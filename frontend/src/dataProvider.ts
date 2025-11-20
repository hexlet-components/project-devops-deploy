import {
  type DataProvider,
  fetchUtils,
  type Identifier,
  type GetListResult,
} from "react-admin";

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

const sortData = (
  data: any[],
  field: string,
  order: "ASC" | "DESC",
): any[] => {
  if (!field) {
    return data;
  }

  return [...data].sort((a, b) => {
    if (a[field] === b[field]) {
      return 0;
    }
    if (a[field] > b[field]) {
      return order === "ASC" ? 1 : -1;
    }
    return order === "ASC" ? -1 : 1;
  });
};

const paginate = (
  data: any[],
  page: number,
  perPage: number,
): GetListResult<any> => {
  const start = (page - 1) * perPage;
  const end = start + perPage;

  return {
    data: data.slice(start, end),
    total: data.length,
  };
};

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

const extractImageFile = (data: Record<string, any>) => {
  const candidate = data?.image;
  if (candidate?.rawFile instanceof File) {
    return candidate.rawFile as File;
  }
  return undefined;
};

const preparePayload = async (data: Record<string, any>) => {
  const payload = { ...data };
  delete payload.image;
  delete payload.imageUrl;

  const rawFile = extractImageFile(data);
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
    const { json } = await httpClient(getCollectionUrl(resource));
    const sorted = sortData(json, params.sort.field, params.sort.order);
    return paginate(sorted, params.pagination.page, params.pagination.perPage);
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
    const { json } = await httpClient(getCollectionUrl(resource));
    const filtered = json.filter(
      (record: Record<string, Identifier>) =>
        record[params.target] === params.id,
    );
    const sorted = sortData(filtered, params.sort.field, params.sort.order);
    return paginate(sorted, params.pagination.page, params.pagination.perPage);
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
