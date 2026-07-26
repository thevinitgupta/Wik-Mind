"use client"

import { archiveWorkspace } from "@/service/workspace.service";
import { useMutation, useQueryClient } from "@tanstack/react-query";

export function useArchiveWorkspace() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: archiveWorkspace,

        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["workspaces"],
            });
        },
    });
}