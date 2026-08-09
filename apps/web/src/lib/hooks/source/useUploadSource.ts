import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { uploadSource } from "@/service/source.service";
import { CreateSourceForm } from "@/types/schema/source.schema";

export function useUploadSource(workspaceId: string) {
  const queryClient = useQueryClient();

  const [progress, setProgress] = useState(0);

  const mutation = useMutation({
    mutationFn: (request: CreateSourceForm) => {
      setProgress(0);

      return uploadSource(workspaceId, request, setProgress);
    },

    onSuccess() {
      setProgress(100);

      queryClient.invalidateQueries({
        queryKey: ["sources", workspaceId],
      });

      queryClient.invalidateQueries({
        queryKey: ["workspace", workspaceId],
      });
    },

    onError() {
      setProgress(0);
    },
  });

  return {
    ...mutation,
    progress,
  };
}
