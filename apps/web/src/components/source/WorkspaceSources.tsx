"use client";

import { Page } from "@/types/commons";
import { SourceResponse, SourceSort } from "@/types/source";

import Loader from "@/components/common/Loader";

import { SourceRow } from "./SourceRow";
import { SourceToolbar } from "./viewer/SourceToolbar";
import { SourcePagination } from "./viewer/SourcePagination";

interface WorkspaceSourcesProps {
  data?: Page<SourceResponse>;

  isLoading: boolean;

  page: number;

  sort: SourceSort;

  onPageChange: (page: number) => void;

  onSortChange: (sort: SourceSort) => void;
}

export default function WorkspaceSources({
  data,
  isLoading,
  page,
  sort,
  onPageChange,
  onSortChange,
}: WorkspaceSourcesProps) {
  if (isLoading) {
    return <Loader />;
  }

  const sources = data?.content ?? [];

  return (
    <div className="space-y-5">

      {/* Toolbar */}
      <SourceToolbar
        sort={sort}
        onSortChange={onSortChange}
      />

      {/* List */}
      {sources.length === 0 ? (
        <div className="rounded-2xl border border-dashed border-white/10 p-10 text-center">
          <h3 className="font-medium">
            No sources yet
          </h3>

          <p className="mt-1 text-sm text-muted-foreground">
            Add a document or URL to start building
            knowledge in this workspace.
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          {sources.map((source) => (
            <SourceRow
              key={source.id}
              source={source}
            />
          ))}
        </div>
      )}

      {/* Pagination */}
      {data && data.totalPages > 1 && (
        <SourcePagination
          page={page}
          totalPages={data.totalPages}
          onPageChange={onPageChange}
        />
      )}

    </div>
  );
}