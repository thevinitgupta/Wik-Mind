import { backendClient } from "@/lib/httpClient";
import { ApiResponse } from "@/types/api";
import { buildPageParams, Page, PageRequest } from "@/types/commons";
import { WorkspaceCreationRequestDTO, WorkspaceResponseDTO } from "@/types/workspace";

export const getUserWorkspaces = async ({
  pageable,
}: {
  pageable?: PageRequest;
}) => {
  const params = pageable ? buildPageParams(pageable) : {};

  const response = await backendClient.get<Page<WorkspaceResponseDTO>>("/api/workspaces", {
    params,
  });
  console.log("Workspace data response",response.data.content);

  return response.data || [];
};

export const getWorkspace = async ({
  workspaceId,
}: {
  workspaceId: string;
}) => {

  const response = await backendClient.get<WorkspaceResponseDTO>(`/api/workspaces/${workspaceId}`);
  console.log("Workspace data response for id:",workspaceId,response.data);

  return response.data || {};
};

export async function createWorkspace(
  request: WorkspaceCreationRequestDTO
) {
  const response = await backendClient.post<
    ApiResponse<WorkspaceResponseDTO>
  >("/api/workspaces", request);

  return response.data.data;
}

export async function toggleWorkspaceStar(workspaceId: string) {
  return backendClient.patch(`/api/workspaces/${workspaceId}/star`);
}

export async function archiveWorkspace(workspaceId: string) {
  return backendClient.patch(`/api/workspaces/${workspaceId}/archive`);
}

export async function restoreWorkspace(workspaceId: string) {
  return backendClient.patch(`/api/workspaces/${workspaceId}/restore`);
}