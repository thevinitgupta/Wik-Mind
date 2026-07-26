"use client";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";

import { GridFourIcon, ListBulletsIcon } from "@phosphor-icons/react";

import { WorkspaceView } from "@/types/workspace";

interface WorkspaceViewToggleProps {
  value: WorkspaceView;

  onChange: (view: WorkspaceView) => void;
}

export function WorkspaceViewToggle({
  value,
  onChange,
}: WorkspaceViewToggleProps) {
  return (
    <ToggleGroup
      type="single"
      value={value}
      onValueChange={(value) => {
        if (!value) return;

        onChange(value as WorkspaceView);
      }}
      className="border border-border bg-card p-1"
    >
      <ToggleGroupItem value={WorkspaceView.LIST} className="min-w-6 w-6 md:w-10 h-6 md:h-10 " aria-label="List View">
        <ListBulletsIcon size={18} />
      </ToggleGroupItem>

      <ToggleGroupItem value={WorkspaceView.GRID} aria-label="Grid View">
        <GridFourIcon size={18} />
      </ToggleGroupItem>
    </ToggleGroup>
  );
}
