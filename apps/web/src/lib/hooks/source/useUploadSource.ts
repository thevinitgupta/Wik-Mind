import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { uploadSource } from "@/service/source.service";
import { CreateSourceForm } from "@/types/schema/source.schema";
import axios from "axios";

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

  const errorMessage = axios.isAxiosError(mutation.error)
    ? mutation.error.response?.data ||
      "Failed to upload source version."
    : mutation.error instanceof Error
      ? mutation.error.message
      : "Failed to upload source version.";

      console.log("Error Message in hook:", errorMessage)

  return {
    ...mutation,
    progress,
    errorMessage,
  };
}
