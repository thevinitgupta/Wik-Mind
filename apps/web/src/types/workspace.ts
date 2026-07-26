export interface WorkspaceResponseDTO {
  id: string;
  name: string;
  visibility: WorkspaceVisibility;
  clonePolicy: WorkspaceClonePolicy;
  starred: boolean;

  status: WorkspaceStatus;
  archivedAt?: string | null;
  updatedAt: string;
}

export enum WorkspaceVisibility {
  PRIVATE,
  PUBLIC,
  UNLISTED,
}

export enum WorkspaceClonePolicy {
  DISABLED,
  MEMBERS_ONLY,
  ANYONE,
}

export enum WorkspaceStatus {
  ACTIVE = "ACTIVE",
  ARCHIVED = "ARCHIVED",
}

export interface WorkspaceCardProps {
  workspace: WorkspaceResponseDTO;

  starred?: boolean;

  progress?: number;

  onClick?: (workspace: WorkspaceResponseDTO) => void;

  onToggleStar?: (workspace: WorkspaceResponseDTO) => void;
}

export interface WorkspaceCreationRequestDTO {
  name: string;
}

export interface WorkspaceInsights {
  sources: number;
  topics: number;
  claims: number;
  notes: number;
}

export enum WorkspaceView {
  GRID = "GRID",
  LIST = "LIST",
}

export enum WorkspaceSort {
  RECENTLY_UPDATED = "RECENTLY_UPDATED",
  ALPHABETICAL = "ALPHABETICAL",
}
