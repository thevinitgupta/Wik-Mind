import { useQuery } from "@tanstack/react-query";

import { fetchWorkspaceSources } from "@/service/source.service";
import { SourceSort } from "@/types/source";

interface UseFetchSourcesProps {
  workspaceId: string;
  page: number;
  size: number;
  sort: SourceSort;
}

export function useFetchSources({
  workspaceId,
  page,
  size,
  sort,
}: UseFetchSourcesProps) {
  return useQuery({
    queryKey: [
      "sources",
      workspaceId,
      page,
      size,
      sort,
    ],

    queryFn: () =>
      fetchWorkspaceSources(
        workspaceId,
        page,
        size,
        sort
      ),

    enabled: !!workspaceId,

    placeholderData: (previousData) => previousData,
  });
}