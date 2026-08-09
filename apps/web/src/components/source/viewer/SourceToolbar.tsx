"use client";

import { SourceSort } from "@/types/source";
import { SourceSortSelect } from "./SourceSortSelect";

interface SourceToolbarProps {
  sort: SourceSort;
  onSortChange: (sort: SourceSort) => void;
}

export function SourceToolbar({
  sort,
  onSortChange,
}: SourceToolbarProps) {
  return (
    <div className="flex items-center justify-end">
      <SourceSortSelect
        value={sort}
        onChange={onSortChange}
      />
    </div>
  );
}