"use client";
import { cn } from "@/lib/utils/tw";
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSkeleton,
} from "../ui/sidebar";
import { BrainIcon } from "@phosphor-icons/react";
import User from "../auth/User";
import { sidebarItems } from "@/config/sidebar";
import { usePathname } from "next/navigation";
import Link from "next/link";

export function AppSidebar() {
  const pathname = usePathname();

  return (
    <Sidebar>
      <SidebarHeader>
        <div
          className={cn(
            "w-full",
            "flex",
            "gap-4",
            "items-center",
            "justify-start",
            "text-lg",
            "font-bold",
            "tracking-tight"
          )}
        >
          <BrainIcon className="cn-rtl-flip" />
          WIK Mind
        </div>
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Navigation</SidebarGroupLabel>

          <SidebarGroupContent>
            <SidebarMenu>
              {sidebarItems.map((item) => {
                const Icon = item.icon;

                return (
                  <SidebarMenuItem key={item.href}>
                    <SidebarMenuButton
                      asChild
                      isActive={
                        (item.href.endsWith("workspaces") &&
                          pathname.includes("workspaces")) ||
                        pathname.endsWith(item.href)
                      }
                    >
                      <Link href={item.href}>
                        <Icon />

                        <span>{item.title}</span>
                      </Link>
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                );
              })}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton>
              <User />
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarFooter>
    </Sidebar>
  );
}
