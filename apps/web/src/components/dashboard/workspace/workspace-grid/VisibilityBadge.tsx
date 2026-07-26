"use client"
import { Badge } from "@/components/ui/badge";
import { WorkspaceVisibility } from "@/types/workspace";
import { GlobeIcon, LinkSimpleIcon, LockIcon } from "@phosphor-icons/react";

export function VisibilityBadge({
    visibility,
  }: {
    visibility: WorkspaceVisibility;
  }) {
    switch (visibility) {
      case WorkspaceVisibility.PUBLIC:
        return (
          <Badge variant="secondary">
            <GlobeIcon className="mr-1 h-3 w-3" />
            Public
          </Badge>
        );
  
      case WorkspaceVisibility.UNLISTED:
        return (
          <Badge variant="secondary">
            <LinkSimpleIcon className="mr-1 h-3 w-3" />
            Unlisted
          </Badge>
        );
  
      default:
        return (
          <Badge variant="secondary">
            <LockIcon className="mr-1 h-3 w-3" />
            Private
          </Badge>
        );
    }
  }