"use client"
import { toggleWorkspaceStar } from "@/service/workspace.service";
import { useQueryClient, useMutation } from "@tanstack/react-query";

export function useToggleWorkspaceStar() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: toggleWorkspaceStar,

        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["workspaces"],
            });
        },
    });
}