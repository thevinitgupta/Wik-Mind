"use client";
import { WorkspaceCard } from "./WorkspaceCard";
import { WorkspaceResponseDTO } from "@/types/workspace";
import { EmptyWorkspaceGrid } from "./EmptyWorkspaceGrid";
import { useRouter } from 'next/navigation';


export function WorkspaceGrid({
  workspaces,
  onWorkspaceClick,
  onToggleStar
}: {
  workspaces: WorkspaceResponseDTO[];
  onWorkspaceClick?: (workspace: WorkspaceResponseDTO) => void;
  onToggleStar: (workspaceId: string) => void;
}) {
  
  if (workspaces.length === 0) {
    return <EmptyWorkspaceGrid />;
  }
  return (
    <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
      {workspaces?.map((workspace) => (
        <WorkspaceCard
          key={workspace.id}
          workspace={workspace}
          onClick={onWorkspaceClick}
          onToggleStar={onToggleStar}
        />
      ))}
    </div>
  );
}
