"use client"
import { getUserWorkspaces } from "@/service/workspace.service";
import { PageRequest, Pagination, Sorting } from "@/types/commons";
import { useQuery } from "@tanstack/react-query";


export function useFetchWorkspaces({
  pageable,
}: {
  pageable?: PageRequest;
}) {
  return useQuery({
    queryKey: ["workspaces", pageable],
    queryFn: () => getUserWorkspaces({ pageable }),
  });
}