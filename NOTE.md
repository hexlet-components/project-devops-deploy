# Image Upload Checks

## Local (dev profile, H2 + temp storage)
1. Start backend: `make run` (uses in-memory H2 and local filesystem storage under `/tmp/bulletin-images`).
2. Start frontend dev server: `cd frontend && npm install && npm run dev`.
3. In React Admin:
   - Create a bulletin or edit an existing one.
   - Use the “Upload image” field; after save, the image preview should load via the generated `imageUrl`.
4. Verify backend log: look for `Stored image` entries or check `/tmp/bulletin-images` for a new file. Refresh the bulletin show page to ensure the presigned/local URL still renders.

## Production / S3
1. Export the required env vars before launching Spring Boot:
   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   export STORAGE_S3_BUCKET=your-bucket
   export STORAGE_S3_REGION=eu-central-1
   export STORAGE_S3_ACCESSKEY=...
   export STORAGE_S3_SECRETKEY=...
   export STORAGE_S3_ENDPOINT=https://s3.eu-central-1.amazonaws.com   # optional
   export STORAGE_S3_CDNURL=https://cdn.example.com/bulletins          # optional
   ```
2. Deploy backend (e.g., `java -jar build/libs/project-devops-deploy-0.0.1-SNAPSHOT.jar`).
3. In the frontend (local or deployed), upload an image for a bulletin.
4. Confirm expected behavior:
   - Response from `/api/files/upload` contains a non-empty `key`.
   - Image shows up in bulletin show view (URL should either point to CDN or be a presigned S3 link).
   - Object exists in S3 bucket (check via AWS console or `aws s3 ls s3://your-bucket/bulletins/...`).
5. Optional: run `curl -I "$(curl -s .../api/files/view?key=... | jq -r .url)"` to ensure the presigned URL is valid from the production environment.
