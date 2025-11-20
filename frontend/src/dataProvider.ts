import { type DataProvider, fetchUtils, type Identifier } from "react-admin";

const apiUrl = import.meta.env.VITE_API_URL ?? "/api";
const fileUploadUrl = `${apiUrl}/files/upload`;

const httpClient = (url: string, options: fetchUtils.Options = {}) => {
  if (!options.headers) {
    options.headers = new Headers({ Accept: "application/json" });
  }

  if (!(options.body instanceof FormData)) {
    (options.headers as Headers).set("Content-Type", "application/json");
  }

  return fetchUtils.fetchJson(url, options);
};

const getCollectionUrl = (resource: string) => `${apiUrl}/${resource}`;
const getRecordUrl = (resource: string, id: Identifier) =>
  `${getCollectionUrl(resource)}/${id}`;

type JsonRecord = Record<string, unknown>;
type MutablePayload = JsonRecord & {
  image?: { rawFile?: unknown };
  imageUrl?: string;
  imageKey?: string;
};

const normalizeCollection = (payload: unknown): JsonRecord[] =>
  Array.isArray(payload) ? (payload as JsonRecord[]) : [];

const uploadImage = async (file?: File) => {
  if (!file) {
    return undefined;
  }

  const formData = new FormData();
  formData.append("file", file);
  const { json } = await httpClient(fileUploadUrl, {
    method: "POST",
    body: formData,
  });
  return json as { key: string; url?: string };
};

const extractImageFile = (data: MutablePayload) => {
  const candidate = data?.image;
  if (candidate?.rawFile instanceof File) {
    return candidate.rawFile;
  }
  return undefined;
};

const preparePayload = async (data: JsonRecord) => {
  const payload = { ...data } as MutablePayload;
  delete payload.image;
  delete payload.imageUrl;

  const rawFile = extractImageFile(payload);
  if (rawFile) {
    const result = await uploadImage(rawFile);
    if (result?.key) {
      payload.imageKey = result.key;
    }
  }

  return payload;
};

export const dataProvider: DataProvider = {
  getList: async (resource, params) => {
    void params;
    const { json } = await httpClient(getCollectionUrl(resource));
    const data = normalizeCollection(json);
    return { data, total: data.length };
  },

  getOne: async (resource, params) => {
    const { json } = await httpClient(getRecordUrl(resource, params.id));
    return { data: json };
  },

  getMany: async (resource, params) => {
    const responses = await Promise.all(
      params.ids.map((id) => httpClient(getRecordUrl(resource, id))),
    );
    return { data: responses.map(({ json }) => json) };
  },

  getManyReference: async (resource, params) => {
    void params;
    const { json } = await httpClient(getCollectionUrl(resource));
    const data = normalizeCollection(json);
    return { data, total: data.length };
  },

  create: async (resource, params) => {
    const body = await preparePayload(params.data);
    const { json } = await httpClient(getCollectionUrl(resource), {
      method: "POST",
      body: JSON.stringify(body),
    });
    return { data: json };
  },

  update: async (resource, params) => {
    const body = await preparePayload(params.data);
    const { json } = await httpClient(getRecordUrl(resource, params.id), {
      method: "PUT",
      body: JSON.stringify(body),
    });
    return { data: json };
  },

  updateMany: async (resource, params) => {
    const responses = await Promise.all(
      params.ids.map((id) =>
        httpClient(getRecordUrl(resource, id), {
          method: "PUT",
          body: JSON.stringify(params.data),
        }),
      ),
    );
    return { data: responses.map(({ json }) => json.id) };
  },

  delete: async (resource, params) => {
    await httpClient(getRecordUrl(resource, params.id), { method: "DELETE" });
    return { data: params.previousData ?? { id: params.id } };
  },

  deleteMany: async (resource, params) => {
    await Promise.all(
      params.ids.map((id) =>
        httpClient(getRecordUrl(resource, id), { method: "DELETE" }),
      ),
    );
    return { data: params.ids };
  },
};
