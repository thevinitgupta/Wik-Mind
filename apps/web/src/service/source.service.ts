import { backendClient } from "@/lib/httpClient";
import { SourceResponse, SourceSort, SourceVersionResponse } from "../types/source";
import { CreateSourceForm } from "@/types/schema/source.schema";
import { Page } from "@/types/commons";
import { CreateSourceVersionForm } from "@/types/schema/create-version.scheme";

export async function fetchWorkspaceSources(
  workspaceId: string,
  page: number,
  size: number,
  sort: SourceSort
): Promise<Page<SourceResponse>> {
  const response = await backendClient.get<
  Page<SourceResponse>
  >(
    `/api/workspaces/${workspaceId}/sources`,
    {
      params: {
        page,
        size,
        sort: getSpringSort(sort),
      },
    }
  );

  return response.data;
}

function getSpringSort(sort: SourceSort): string {
  switch (sort) {
    case SourceSort.CREATED_AT_ASC:
      return "createdAt,asc";

    case SourceSort.ALPHABETICAL:
      return "name,asc";

    case SourceSort.CREATED_AT_DESC:
    default:
      return "createdAt,desc";
  }
}

export async function uploadSource(
  workspaceId: string,
  data: CreateSourceForm,
  onProgress?: (progress: number) => void
): Promise<SourceResponse> {
  const formData = new FormData();

  formData.append("sourceType", data.sourceType);
  formData.append("displayName", data.displayName || "");

  if (data.multipartFile) {
    formData.append("multipartFile", data.multipartFile);
  }

  if (data.url) {
    formData.append("url", data.url);
  }

  const response = await backendClient.post(
    `/api/workspaces/${workspaceId}/sources`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },

      onUploadProgress(event) {
        if (!event.total) return;

        onProgress?.(Math.round((event.loaded * 100) / event.total));
      },
    }
  );

  return response.data;
}

export async function uploadSourceVersion(
  workspaceId: string,
  sourceId: string,
  data: CreateSourceVersionForm,
  onProgress?: (progress: number) => void
): Promise<SourceVersionResponse> {
  const formData = new FormData();

  formData.append(
    "displayName",
    data.displayName || ""
  );

  if (data.multipartFile) {
    formData.append(
      "multipartFile",
      data.multipartFile
    );
  }

  const response = await backendClient.post(
    `/api/workspaces/${workspaceId}/sources/${sourceId}/versions`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },

      onUploadProgress(event) {
        if (!event.total) return;

        onProgress?.(
          Math.round(
            (event.loaded * 100) / event.total
          )
        );
      },
    }
  );

  return response.data;
}
