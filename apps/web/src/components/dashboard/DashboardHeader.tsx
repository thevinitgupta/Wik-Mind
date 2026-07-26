"use client";
import User from "../auth/User";
import { Input } from "../ui/input";
import { SidebarTrigger } from "../ui/sidebar";

export function DashboardHeader() {
  return (
    <header className="sticky top-0 flex h-16 items-center justify-between border-b px-6 backdrop-blur">
      <SidebarTrigger />
      <div className="ml-4 flex-1 flex justify-between">
        <Input placeholder="Search" className="flex-1" />
        <User />
      </div>
    </header>
  );
}
