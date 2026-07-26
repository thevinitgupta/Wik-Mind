"use client";
import Loader from "@/components/common/Loader";
import { AppSidebar } from "@/components/dashboard/AppSidebar";
import { DashboardHeader } from "@/components/dashboard/DashboardHeader";
import { SidebarProvider } from "@/components/ui/sidebar";
import { useRequireAuth } from "@/lib/hooks/useRequireAuth";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const { isLoading } = useRequireAuth();
  if (isLoading) {
    return <Loader className="text-3xl" />;
  }
  return (
    <SidebarProvider>
      <div className="flex min-h-screen bg-background w-full">
        <AppSidebar />
        <main className="flex-1">
          <DashboardHeader />
          <div className="p-6">{children}</div>
        </main>
      </div>
    </SidebarProvider>
  );
}
