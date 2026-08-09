"use client";

import { Button } from "@/components/ui/button";

import {
  CaretLeftIcon,
  CaretRightIcon,
} from "@phosphor-icons/react";

interface SourcePaginationProps {
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export function SourcePagination({
  page,
  totalPages,
  onPageChange,
}: SourcePaginationProps) {
  if (totalPages <= 1) {
    return null;
  }

  return (
    <div className="flex items-center justify-between pt-2">
      <p className="text-xs text-muted-foreground">
        Page {page + 1} of {totalPages}
      </p>

      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="icon"
          disabled={page === 0}
          onClick={() => onPageChange(page - 1)}
        >
          <CaretLeftIcon size={16} />
        </Button>

        <Button
          variant="outline"
          size="icon"
          disabled={page >= totalPages - 1}
          onClick={() => onPageChange(page + 1)}
        >
          <CaretRightIcon size={16} />
        </Button>
      </div>
    </div>
  );
}