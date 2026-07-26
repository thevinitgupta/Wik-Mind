"use client"
import { getWorkspace } from "@/service/workspace.service";
import { useQuery } from "@tanstack/react-query";


export function useFetchWorkspace({
  workspaceId
}: {
  workspaceId: string;
}) {
  return useQuery({
    queryKey: ["workspace", workspaceId],
    queryFn: () => getWorkspace({ workspaceId }),
  });
}