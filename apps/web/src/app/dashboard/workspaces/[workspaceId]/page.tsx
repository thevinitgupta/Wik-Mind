"use client";

import Loader from "@/components/common/Loader";
import { useFetchWorkspace } from "@/lib/hooks/workspace/useFetchWorkspace";
import { use } from "react";

export default function WorkspacesPage({
  params,
}: {
  params: Promise<{ workspaceId: string }>;
}) {
  const { workspaceId } = use(params);
  const { data: workspaceData, isLoading } = useFetchWorkspace({
    workspaceId,
  });
  

  if (isLoading) return <Loader />;

  console.log("workspace data : ", workspaceData);
  return (
    <div className="grid gap-6">
      <h1 className="text-3xl font-bold">{workspaceData?.name}</h1>
    </div>
  );
}
