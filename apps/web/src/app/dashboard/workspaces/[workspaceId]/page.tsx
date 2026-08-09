"use client";

import { use, useState } from "react";

import { Button } from "@/components/ui/button";
import { PlusIcon } from "@phosphor-icons/react";

import Loader from "@/components/common/Loader";

import UploadSourceDialog from "@/components/source/upload-dialog/UploadSourceDialog";

import WorkspaceSources from "@/components/source/WorkspaceSources";

import { WorkspaceInsights } from "@/components/dashboard/workspace/workspace-insights/insights";

import { useFetchWorkspace } from "@/lib/hooks/workspace/useFetchWorkspace";

import { useFetchSources } from "@/lib/hooks/source/useFetchSources";

import { SourceSort } from "@/types/source";

export default function WorkspacesPage({
  params,
}: {
  params: Promise<{ workspaceId: string }>;
}) {
  const { workspaceId } = use(params);

  const { data: workspaceData, isLoading: workspaceLoading } =
    useFetchWorkspace({
      workspaceId,
    });

  const [sourcePage, setSourcePage] = useState(0);

  const [sourceSort, setSourceSort] = useState<SourceSort>(
    SourceSort.CREATED_AT_DESC
  );

  const { data: sourceData, isLoading: sourcesLoading } = useFetchSources({
    workspaceId,
    page: sourcePage,
    size: 5,
    sort: sourceSort,
  });

  const [uploadDialogOpen, setUploadDialogOpen] = useState(false);

  if (workspaceLoading) {
    return <Loader />;
  }

  const handleSourceSortChange = (sort: SourceSort) => {
    setSourceSort(sort);

    // When sorting changes, return
    // to the first page.
    setSourcePage(0);
  };

  return (
    <main className="space-y-8 p-6">
      {/* Header */}
      <section className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold tracking-tight">
            {workspaceData?.name}
          </h1>

          <p className="mt-1 text-sm text-muted-foreground">
            Knowledge and sources for this workspace
          </p>
        </div>

        <Button onClick={() => setUploadDialogOpen(true)}>
          <PlusIcon className="mr-2 h-4 w-4" />
          Add Source
        </Button>
      </section>

      {/* Insights */}
      <section>
        <WorkspaceInsights sourceCount={sourceData?.totalElements ?? 0} />
      </section>

      {/* Sources */}
      <section>
        <div className="mb-4">
          <h2 className="text-lg font-semibold">Sources</h2>

          <p className="text-sm text-muted-foreground">
            Documents and resources connected to this workspace.
          </p>
        </div>

        <WorkspaceSources
          data={sourceData}
          isLoading={sourcesLoading}
          page={sourcePage}
          sort={sourceSort}
          onPageChange={setSourcePage}
          onSortChange={handleSourceSortChange}
        />
      </section>

      {/* Upload */}
      <UploadSourceDialog
        workspaceId={workspaceId}
        open={uploadDialogOpen}
        onOpenChange={setUploadDialogOpen}
      />
    </main>
  );
}
