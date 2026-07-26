// utils/workspace-theme.ts

export interface WorkspaceTheme {
    bg: string;
    border: string;
    iconBg: string;
    accent: string;
  }
  
  const themes: WorkspaceTheme[] = [
    {
      bg: "bg-emerald-950/40",
      border: "border-emerald-700/30",
      iconBg: "bg-emerald-500/20",
      accent: "bg-emerald-400"
    },
    {
      bg: "bg-cyan-950/40",
      border: "border-cyan-700/30",
      iconBg: "bg-cyan-500/20",
      accent: "bg-cyan-400"
    },
    {
      bg: "bg-violet-950/40",
      border: "border-violet-700/30",
      iconBg: "bg-violet-500/20",
      accent: "bg-violet-400"
    },
    {
      bg: "bg-amber-950/40",
      border: "border-amber-700/30",
      iconBg: "bg-amber-500/20",
      accent: "bg-amber-400"
    },
    {
      bg: "bg-rose-950/40",
      border: "border-rose-700/30",
      iconBg: "bg-rose-500/20",
      accent: "bg-rose-400"
    },
    {
      bg: "bg-indigo-950/40",
      border: "border-indigo-700/30",
      iconBg: "bg-indigo-500/20",
      accent: "bg-indigo-400"
    }
  ];
  
  export function getWorkspaceTheme(id: string): WorkspaceTheme {
    let hash = 0;
  
    for (const char of id) {
      hash = (hash * 31 + char.charCodeAt(0)) >>> 0;
    }
  
    return themes[hash % themes.length];
  }