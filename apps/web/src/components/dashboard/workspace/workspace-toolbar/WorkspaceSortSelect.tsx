"use client"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

import { WorkspaceSort } from "@/types/workspace";

interface WorkspaceSortSelectProps {
  value: WorkspaceSort;

  onChange: (sort: WorkspaceSort) => void;
}

export function WorkspaceSortSelect({
  value,
  onChange,
}: WorkspaceSortSelectProps) {
  return (
    <Select
      value={value}
      onValueChange={(value) => onChange(value as WorkspaceSort)}
    >
      <SelectTrigger className="w-24 truncate md:w-56 text-xs md:text-base">
        <SelectValue />
      </SelectTrigger>

      <SelectContent>
        <SelectItem value={WorkspaceSort.RECENTLY_UPDATED}>
          Recently Updated
        </SelectItem>

        <SelectItem value={WorkspaceSort.ALPHABETICAL}>Alphabetical</SelectItem>
      </SelectContent>
    </Select>
  );
}
