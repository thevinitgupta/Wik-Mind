"use client"
import { WorkspaceResponseDTO } from "@/types/workspace";
import { WorkspaceListRow } from "./WorkspaceListRow";

interface WorkspaceListProps {
  workspaces: WorkspaceResponseDTO[];
  onWorkspaceClick?: (workspace: WorkspaceResponseDTO) => void;
  onToggleStar: (workspaceId: string) => void;
}

export function WorkspaceList({ workspaces, onWorkspaceClick, onToggleStar }: WorkspaceListProps) {
  return (
    <div className="space-y-4">
      {workspaces.map((workspace) => (
        <WorkspaceListRow key={workspace.id} workspace={workspace} onClick={onWorkspaceClick} onToggleStar={onToggleStar} />
      ))}
    </div>
  );
}
