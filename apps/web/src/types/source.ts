export enum SourceType {
  FILE = "FILE",
  URL = "URL",
}

export enum SourceStatus {
    PENDING = "PENDING",
    UPLOADED = "UPLOADED",
    PROCESSING = "PROCESSING",
    READY = "READY",
    FAILED = "FAILED",
}

export enum SourceSort {
  CREATED_AT_DESC = "CREATED_AT_DESC",
  CREATED_AT_ASC = "CREATED_AT_ASC",
  ALPHABETICAL = "ALPHABETICAL",
}

export interface SourceVersionSummary {
  id: string;
  versionNumber: number;
  name: string;
  status: SourceStatus;
  mimeType?: string;
  size?: number;
  createdAt: string;
}

export interface SourceResponse {
  id: string;
  workspaceId: string;
  name: string;
  type: SourceType;
  latestVersion: SourceVersionSummary | null;
  createdAt: string;
  updatedAt?: string;
}

export interface Instant {
  EPOCH: Instant;
  MIN_SECOND: number;
  MAX_SECOND: number;
  MIN: Instant;
  MAX: Instant;
  seconds: number;
  nanos: number;
}

export interface SourceVersionResponse {
  id: string;
  sourceId: string;
  workspaceId: string;
  versionNumber: number;
  name: string;
  type: SourceType;
  status: SourceStatus;
  mimeType?: string;
  size?: number;
  createdAt: string;
}