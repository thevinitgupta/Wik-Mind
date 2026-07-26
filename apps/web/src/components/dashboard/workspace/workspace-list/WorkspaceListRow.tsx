"use client";
import { Card } from "@/components/ui/card";
import { FolderIcon, StarIcon } from "@phosphor-icons/react";
import { VisibilityBadge } from "../workspace-grid/VisibilityBadge";
import { CloneBadge } from "../workspace-grid/CloneBadge";
import { getWorkspaceTheme } from "@/lib/utils/workspace-theme";
import { formatDate } from "@/lib/utils/date";
import { WorkspaceResponseDTO } from "@/types/workspace";
import { useToggleWorkspaceStar } from "@/lib/hooks/workspace/useToggleWorkspaceStar";

interface WorkspaceListRowProps {
  workspace: WorkspaceResponseDTO;
  starred?: boolean;

  onClick?: (workspace: WorkspaceResponseDTO) => void;

  onToggleStar: (workspaceId: string) => void;
}

export function WorkspaceListRow({
  workspace,
  onClick,
  onToggleStar
}: WorkspaceListRowProps) {
  const theme = getWorkspaceTheme(workspace.id);
  return (
    <Card
      onClick={() => onClick?.(workspace)}
      className="
      cursor-pointer
      rounded-2xl
      border-white/5
      transition-all
      hover:border-white/10
      hover:bg-card/70
      "
    >
      <div className="flex items-center gap-6 px-6 py-0">
        <div
          className={`
            flex
            h-12
            w-12
            shrink-0
            items-center
            justify-center
            rounded-xl
            ${theme.iconBg}
          `}
        >
          <FolderIcon size={26} className="text-white" />
        </div>

        <div className="min-w-0 flex-1">
          <h3 className="truncate text-lg font-semibold">{workspace.name}</h3>

          <p className="mt-1 text-xs text-muted-foreground">
            Knowledge Workspace
          </p>
        </div>

        <div className="hidden gap-2 lg:flex">
          <VisibilityBadge visibility={workspace.visibility} />

          <CloneBadge clonePolicy={workspace.clonePolicy} />
        </div>

        <div className="hidden w-36 text-right lg:block">
          <p className="text-xs text-muted-foreground">Last Updated</p>

          <p className="mt-1 text-sm">{formatDate(workspace.updatedAt)}</p>
        </div>

        <button
          onClick={(e) => {
            e.stopPropagation();
            onToggleStar(workspace.id);
          }}
        >
          <StarIcon
            size={20}
            weight={workspace.starred ? "fill" : "regular"}
            className={workspace.starred ? "text-yellow-400" : "text-muted-foreground"}
          />
        </button>
      </div>
    </Card>
  );
}
