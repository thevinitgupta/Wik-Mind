import { CreateWorkspace } from "@/components/dashboard/workspace/CreateWorkspace";
import { WorkspaceInsights } from "@/components/dashboard/workspace/workspace-insights/insights";

export default function DashboardPage() {
  return (
    <div className="grid gap-6">
      <div className="w-full flex justify-between items-center">
        <h1 className="text-3xl font-bold flex-1">Dashboard</h1>
        <CreateWorkspace />
      </div>
      <WorkspaceInsights />
    </div>
  );
}
