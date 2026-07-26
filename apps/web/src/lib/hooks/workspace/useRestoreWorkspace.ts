"use client"

import { restoreWorkspace } from "@/service/workspace.service";
import { useQueryClient, useMutation } from "@tanstack/react-query";

export function useRestoreWorkspace() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: restoreWorkspace,

        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["workspaces"],
            });
        },
    });
}