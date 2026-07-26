import { cn } from "@/lib/utils/tw";
import { CreateWorkspace } from "../CreateWorkspace";

export function EmptyWorkspaceGrid() {
  return (
    <div className={cn("w-full", "h-[45vh]", "text-center", "pt-24")}>
      <h3 className="mb-6">No workspaces found for user</h3>
      <CreateWorkspace />
    </div>
  );
}
