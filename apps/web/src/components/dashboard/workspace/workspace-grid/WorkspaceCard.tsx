import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

import { Progress } from "@/components/ui/progress";

import { FolderIcon, StarIcon } from "@phosphor-icons/react";

import { getWorkspaceTheme } from "@/lib/utils/workspace-theme";
import { WorkspaceResponseDTO } from "@/types/workspace";
import { VisibilityBadge } from "./VisibilityBadge";
import { CloneBadge } from "./CloneBadge";
import { formatDate } from "@/lib/utils/date";

interface WorkspaceCardProps {
  workspace: WorkspaceResponseDTO;
  progress?: number;
  onClick?: (workspace: WorkspaceResponseDTO) => void;
  onToggleStar: (workspaceId: string) => void;
}

export function WorkspaceCard({
  workspace,
  progress,
  onClick,
  onToggleStar
}: WorkspaceCardProps) {
  const theme = getWorkspaceTheme(workspace.id);

  return (
    <Card
      onClick={() => onClick?.(workspace)}
      className={`
        relative
        h-56
        cursor-pointer
        rounded-3xl
        border
        border-white/8
        ${theme.bg}
        transition-all
        duration-300
        hover:-translate-y-1
        hover:border-white/15
        hover:shadow-2xl
      `}
    >
      {/* Star */}
      <button
        onClick={(e) => {
          e.stopPropagation();
          onToggleStar(workspace.id);
        }}
        className="absolute right-6 top-6 z-10"
      >
        <StarIcon
          size={22}
          weight={workspace.starred ? "fill" : "regular"}
          className={
            workspace.starred
              ? "text-yellow-400"
              : "text-muted-foreground hover:text-white"
          }
        />
      </button>

      <CardHeader className="pb-2">
        <div className="flex items-center gap-5">
          <div
            className={`
              flex
              h-12
              w-12
              shrink-0
              items-center
              justify-center
              rounded-2xl
              ${theme.iconBg}
            `}
          >
            <FolderIcon size={28} weight="regular" className="text-white" />
          </div>

          <div className="min-w-0">
            <CardTitle className="truncate text-xl font-semibold normal-case tracking-normal w-4/5">
              {workspace.name}
            </CardTitle>

            <p className="mt-1 text-xs text-muted-foreground">
              Knowledge Workspace
            </p>
          </div>
        </div>
      </CardHeader>

      <CardContent className="flex h-full flex-col">
        <div className="mt-auto space-y-5">
          <div className="flex flex-wrap gap-2">
            <VisibilityBadge visibility={workspace.visibility} />
            <CloneBadge clonePolicy={workspace.clonePolicy} />
          </div>

          {progress !== undefined && (
            <div className="space-y-2">
              <div className="flex justify-between text-xs text-muted-foreground">
                <span>Progress</span>
                <span>{progress}%</span>
              </div>

              <Progress value={progress} />
            </div>
          )}

          <div className="flex items-center justify-between border-t border-white/5 pt-4 text-xs text-muted-foreground">
            <span>Last Updated</span>

            <span className="font-mono">{formatDate(workspace.updatedAt)}</span>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
