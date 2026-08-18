import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { uploadSourceVersion } from "@/service/source.service";
import { CreateSourceVersionForm } from "@/types/schema/create-version.scheme";
import axios from "axios";

export function useUploadSourceVersion(workspaceId: string, sourceId: string) {
  const queryClient = useQueryClient();

  const [progress, setProgress] = useState(0);

  const mutation = useMutation({
    mutationFn: (request: CreateSourceVersionForm) => {
      setProgress(0);

      return uploadSourceVersion(workspaceId, sourceId, request, setProgress);
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
