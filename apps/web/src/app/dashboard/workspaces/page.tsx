"use client";

import Loader from "@/components/common/Loader";
import { WorkspaceGrid } from "@/components/dashboard/workspace/workspace-grid/WorkspaceGrid";
import { WorkspaceList } from "@/components/dashboard/workspace/workspace-list/WorkspaceList";
import { WorkspaceToolbar } from "@/components/dashboard/workspace/workspace-toolbar/WorkspaceToolbar";
import { useFetchWorkspaces } from "@/lib/hooks/workspace/useFetchWorkspaces";
import { useToggleWorkspaceStar } from "@/lib/hooks/workspace/useToggleWorkspaceStar";
import { WorkspaceView, WorkspaceSort, WorkspaceResponseDTO } from "@/types/workspace";
import { useRouter } from "next/navigation";
import { useState, useMemo } from "react";

export default function WorkspacesPage() {
  const [view, setView] = useState(WorkspaceView.GRID);
  const [sort, setSort] = useState(WorkspaceSort.RECENTLY_UPDATED);

  const { data: page, isLoading } = useFetchWorkspaces({
    pageable: {
      page: 0,
      size: 12,
    },
  });
  const { mutate: toggleStar, isPending } = useToggleWorkspaceStar();


  const router = useRouter();
  
    const handleNavigation = (workspace : WorkspaceResponseDTO) => {
      router.push('/dashboard/workspaces/'+workspace.id); 
    };

  const workspaces = useMemo(() => {
    const copy = [...(page?.content ?? [])];

    switch (sort) {
      case WorkspaceSort.ALPHABETICAL:
        copy.sort((a, b) => a.name.localeCompare(b.name));
        break;

      case WorkspaceSort.RECENTLY_UPDATED:
      default:
        copy.sort(
          (a, b) =>
            new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime()
        );
    }

    return copy;
  }, [page, sort]);

  if (isLoading) return <Loader />;

  return (
    <div className="space-y-6">
      <div className="flex items-start md:items-center justify-between">
        <div>
          <h1 className="text-xl md:text-3xl font-bold">My Workspaces</h1>

          <p className="text-xs md:text-base text-muted-foreground">
            All your knowledge workspaces in one place.
          </p>
        </div>

        <WorkspaceToolbar
          view={view}
          sort={sort}
          onViewChange={setView}
          onSortChange={setSort}
        />
      </div>

      {view === WorkspaceView.GRID ? (
        <WorkspaceGrid workspaces={workspaces} onWorkspaceClick={handleNavigation} onToggleStar={toggleStar} />
      ) : (
        <WorkspaceList workspaces={workspaces} onWorkspaceClick={handleNavigation} onToggleStar={toggleStar} />
      )}
    </div>
  );
}
