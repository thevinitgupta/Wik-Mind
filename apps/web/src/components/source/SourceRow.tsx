"use client";

import { useState } from "react";

import { formatDate } from "@/lib/utils/date";
import { SourceResponse, SourceStatus } from "@/types/source";

import { LinkIcon, FileTextIcon, PlusIcon } from "@phosphor-icons/react";

import { Button } from "@/components/ui/button";

import AddSourceVersionDialog from "./upload-dialog/AddSourceVersionDialog";

function formatFileSize(size: number) {
  if (size < 1024) {
    return `${size} B`;
  }

  if (size < 1024 * 1024) {
    return `${(size / 1024).toFixed(1)} KB`;
  }

  if (size < 1024 * 1024 * 1024) {
    return `${(size / (1024 * 1024)).toFixed(1)} MB`;
  }

  return `${(size / (1024 * 1024 * 1024)).toFixed(1)} GB`;
}

function SourceStatusBadge({ status }: { status: SourceStatus }) {
  const styles: Record<SourceStatus, string> = {
    [SourceStatus.PENDING]: "bg-slate-500/10 text-slate-400",

    [SourceStatus.UPLOADED] : "bg-blue-500/10 text-blue-400",

    [SourceStatus.PROCESSING]: "bg-amber-500/10 text-amber-400",

    [SourceStatus.READY]: "bg-emerald-500/10 text-emerald-400",

    [SourceStatus.FAILED]: "bg-red-500/10 text-red-400",
  
  };

  return (
    <span
      className={`
        rounded-full
        px-3
        py-1
        text-xs
        font-medium
        ${styles[status]}
      `}
    >
      {status}
    </span>
  );
}

export function SourceRow({
  source,
  workspaceId,
}: {
  source: SourceResponse;
  workspaceId: string;
}) {
  const [versionDialogOpen, setVersionDialogOpen] = useState(false);

  const latestVersion = source.latestVersion;

  const icon =
    source.type === "URL" ? (
      <LinkIcon size={22} weight="duotone" />
    ) : (
      <FileTextIcon size={22} weight="duotone" />
    );

  return (
    <>
      <div
        className="
          group flex items-center gap-4
          rounded-2xl
          border border-white/5
          bg-card
          p-4
          transition-all
          duration-200
          hover:border-white/10
          hover:bg-accent/30
        "
      >
        {/* Icon */}

        <div
          className="
            flex h-11 w-11 shrink-0
            items-center justify-center
            rounded-xl
            bg-emerald-500/10
            text-emerald-400
          "
        >
          {icon}
        </div>

        {/* Source information */}

        <div className="min-w-0 flex-1">
          <p className="truncate font-medium">{source.name}</p>

          <div
            className="
              mt-1 flex items-center gap-2
              text-xs text-muted-foreground
            "
          >
            <span>{source.type}</span>

            {latestVersion?.versionNumber && (
              <>
                <span>•</span>

                <span>v{latestVersion.versionNumber}</span>
              </>
            )}

            {latestVersion?.mimeType && (
              <>
                <span>•</span>

                <span>{latestVersion.mimeType}</span>
              </>
            )}

            {latestVersion?.size && latestVersion.size > 0 && (
              <>
                <span>•</span>

                <span>{formatFileSize(latestVersion.size)}</span>
              </>
            )}
          </div>
        </div>

        {/* Status */}

        {latestVersion && <SourceStatusBadge status={latestVersion.status} />}

        {/* Last updated */}

        <span
          className="
            hidden
            text-xs
            text-muted-foreground
            sm:block
          "
        >
          {formatDate(latestVersion?.createdAt ?? source.createdAt)}
        </span>

        {/* Add version */}

        <Button
          type="button"
          variant="ghost"
          size="sm"
          className="
            shrink-0
            gap-2
            opacity-0
            transition-opacity
            group-hover:opacity-100
          "
          onClick={() => setVersionDialogOpen(true)}
        >
          <PlusIcon size={16} />

          <span className="hidden lg:inline">New version</span>
        </Button>
      </div>

      <AddSourceVersionDialog
        open={versionDialogOpen}
        onOpenChange={setVersionDialogOpen}
        workspaceId={workspaceId}
        sourceId={source.id}
        sourceName={source.name}
      />
    </>
  );
}
