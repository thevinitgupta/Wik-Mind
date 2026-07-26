import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createWorkspace } from "@/service/workspace.service";

export function useCreateWorkspace() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createWorkspace,

    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["workspaces"],
      });
    },
  });
}