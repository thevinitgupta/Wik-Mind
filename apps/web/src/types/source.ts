export enum SourceType {
    FILE = "FILE",
    URL = "URL"
}

export enum SourceStatus {
    PROCESSING = "PROCESSING",
    READY = "READY",
    FAILED = "FAILED"
}

export enum SourceSort {
    CREATED_AT_DESC = "CREATED_AT_DESC",
    CREATED_AT_ASC = "CREATED_AT_ASC",
    ALPHABETICAL = "ALPHABETICAL",
  }

export interface SourceResponse {
    id: string
    workspaceId: string
    name: string
    type: SourceType
    status: SourceStatus
    mimeType: string
    size: number
    createdAt: string
}