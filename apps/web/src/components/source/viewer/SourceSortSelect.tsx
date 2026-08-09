"use client";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

import { SourceSort } from "@/types/source";

interface SourceSortSelectProps {
  value: SourceSort;
  onChange: (sort: SourceSort) => void;
}

export function SourceSortSelect({
  value,
  onChange,
}: SourceSortSelectProps) {
  return (
    <Select
      value={value}
      onValueChange={(value) =>
        onChange(value as SourceSort)
      }
    >
      <SelectTrigger className="w-44 text-xs md:text-sm">
        <SelectValue />
      </SelectTrigger>

      <SelectContent>
        <SelectItem value={SourceSort.CREATED_AT_DESC}>
          Newest Created
        </SelectItem>

        <SelectItem value={SourceSort.CREATED_AT_ASC}>
          Oldest Created
        </SelectItem>

        <SelectItem value={SourceSort.ALPHABETICAL}>
          Alphabetical
        </SelectItem>
      </SelectContent>
    </Select>
  );
}