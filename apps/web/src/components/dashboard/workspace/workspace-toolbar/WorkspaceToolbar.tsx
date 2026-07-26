"use client";

import { WorkspaceView } from "@/types/workspace";
import { WorkspaceSort } from "@/types/workspace";

import { WorkspaceViewToggle } from "./WorkspaceViewToggle";
import { WorkspaceSortSelect } from "./WorkspaceSortSelect";

interface WorkspaceToolbarProps {
  view: WorkspaceView;

  sort: WorkspaceSort;

  onViewChange: (view: WorkspaceView) => void;

  onSortChange: (sort: WorkspaceSort) => void;
}

export function WorkspaceToolbar({
  view,
  sort,
  onViewChange,
  onSortChange,
}: WorkspaceToolbarProps) {
  return (
    <div className="flex flex-col md:flex-row items-start md:items-center justify-end gap-3">
      <WorkspaceViewToggle value={view} onChange={onViewChange} />

      <WorkspaceSortSelect value={sort} onChange={onSortChange} />
    </div>
  );
}
