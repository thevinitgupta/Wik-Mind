"use client"
import { WorkspaceClonePolicy } from "@/types/workspace";
import { Badge } from "@/components/ui/badge";
import { CopyIcon } from "@phosphor-icons/react";

export function CloneBadge({
    clonePolicy,
  }: {
    clonePolicy: WorkspaceClonePolicy;
  }) {
    switch (clonePolicy) {
      case WorkspaceClonePolicy.ANYONE:
        return (
          <Badge variant="outline">
            <CopyIcon className="mr-1 h-3 w-3" />
            Cloneable
          </Badge>
        );
  
      case WorkspaceClonePolicy.MEMBERS_ONLY:
        return (
          <Badge variant="outline">
            <CopyIcon className="mr-1 h-3 w-3" />
            Members
          </Badge>
        );
  
      default:
        return (
          <Badge variant="outline">
            <CopyIcon className="mr-1 h-3 w-3" />
            No Clone
          </Badge>
        );
    }
  }