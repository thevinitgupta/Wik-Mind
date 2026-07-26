import { BookOpenIcon, FolderNotchOpenIcon, GearIcon, HouseIcon, NetworkIcon } from "@phosphor-icons/react";

export interface SidebarItem {
  title: string;
  href: string;
  icon: React.ElementType;
}

export const sidebarItems: SidebarItem[] = [
  {
    title: "Home",
    href: "/dashboard",
    icon: HouseIcon,
  },
  {
    title: "My Workspaces",
    href: "/dashboard/workspaces",
    icon: FolderNotchOpenIcon,
  },
  {
    title: "Graph Explorer",
    href: "/dashboard/explorer",
    icon: NetworkIcon,
  },
  {
    title: "Sources",
    href: "/dashboard/sources",
    icon: BookOpenIcon,
  },
  {
    title: "Settings",
    href: "/dashboard/settings",
    icon: GearIcon,
  },
];