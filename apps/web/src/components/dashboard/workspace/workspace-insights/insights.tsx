"use client";

import {
  FileTextIcon,
  BrainIcon,
  ChatCircleTextIcon,
  NoteIcon,
} from "@phosphor-icons/react";

import { WorkspaceInsightCard } from "./insight-card";

interface WorkspaceInsightsProps {
  sourceCount: number;
}

export function WorkspaceInsights({
  sourceCount,
}: WorkspaceInsightsProps) {
  return (
    <section className="workspace-insights-grid">
      <WorkspaceInsightCard
        title="Sources"
        value={sourceCount}
        subtitle="PDFs • URLs • Text"
        accentClass="bg-emerald-500/15 text-emerald-400"
        icon={
          <FileTextIcon
            size={24}
            weight="duotone"
          />
        }
      />

      <WorkspaceInsightCard
        title="Topics"
        value={18}
        subtitle="Knowledge Concepts"
        accentClass="bg-violet-500/15 text-violet-400"
        icon={
          <BrainIcon
            size={24}
            weight="duotone"
          />
        }
      />

      <WorkspaceInsightCard
        title="Claims"
        value={64}
        subtitle="Knowledge Units"
        accentClass="bg-amber-500/15 text-amber-400"
        icon={
          <ChatCircleTextIcon
            size={24}
            weight="duotone"
          />
        }
      />

      <WorkspaceInsightCard
        title="Notes"
        value={8}
        subtitle="Personal Notes"
        accentClass="bg-sky-500/15 text-sky-400"
        icon={
          <NoteIcon
            size={24}
            weight="duotone"
          />
        }
      />
    </section>
  );
}