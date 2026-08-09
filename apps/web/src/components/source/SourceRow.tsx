import { formatDate } from "@/lib/utils/date";
import { SourceResponse, SourceStatus } from "@/types/source";
import { LinkIcon, FileTextIcon } from "@phosphor-icons/react";

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

function SourceStatusBadge({
    status,
  }: {
    status: SourceStatus;
  }) {
    const styles = {
      [SourceStatus.READY]:
        "bg-emerald-500/10 text-emerald-400",
  
      [SourceStatus.PROCESSING]:
        "bg-amber-500/10 text-amber-400",
  
      [SourceStatus.FAILED]:
        "bg-red-500/10 text-red-400",
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
  }: {
    source: SourceResponse;
  }) {
    const icon =
      source.type === "URL"
        ? (
          <LinkIcon
            size={22}
            weight="duotone"
          />
        )
        : (
          <FileTextIcon
            size={22}
            weight="duotone"
          />
        );
  
    return (
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
  
        <div className="min-w-0 flex-1">
          <p className="truncate font-medium">
            {source.name}
          </p>
  
          <div className="mt-1 flex items-center gap-2 text-xs text-muted-foreground">
            <span>{source.type}</span>
  
            {source.mimeType && (
              <>
                <span>•</span>
                <span>{source.mimeType}</span>
              </>
            )}
  
            {source.size > 0 && (
              <>
                <span>•</span>
                <span>
                  {formatFileSize(source.size)}
                </span>
              </>
            )}
          </div>
        </div>
  
        <SourceStatusBadge status={source.status} />
  
        <span className="hidden text-xs text-muted-foreground sm:block">
          {formatDate(source.createdAt)}
        </span>
      </div>
    );
  }