"use client"
import {
    FileTextIcon,
    BrainIcon,
    ChatCircleTextIcon,
    NoteIcon,
  } from "@phosphor-icons/react";
  
  import { WorkspaceInsightCard } from "./insight-card";
  
  const placeholderInsights = {
    sources: 12,
    topics: 18,
    claims: 64,
    notes: 8,
  };
  
  export function WorkspaceInsights() {
    return (
      <section className="grid gap-5 md:grid-cols-2 xl:grid-cols-4">
  
        <WorkspaceInsightCard
          title="Sources"
          value={placeholderInsights.sources}
          subtitle="PDFs • URLs • Text"
          accentClass="bg-emerald-500/15 text-emerald-400"
          icon={<FileTextIcon size={24} weight="duotone" />}
        />
  
        <WorkspaceInsightCard
          title="Topics"
          value={placeholderInsights.topics}
          subtitle="Knowledge Concepts"
          accentClass="bg-violet-500/15 text-violet-400"
          icon={<BrainIcon size={24} weight="duotone" />}
        />
  
        <WorkspaceInsightCard
          title="Claims"
          value={placeholderInsights.claims}
          subtitle="Knowledge Units"
          accentClass="bg-amber-500/15 text-amber-400"
          icon={<ChatCircleTextIcon size={24} weight="duotone" />}
        />
  
        <WorkspaceInsightCard
          title="Notes"
          value={placeholderInsights.notes}
          subtitle="Personal Notes"
          accentClass="bg-sky-500/15 text-sky-400"
          icon={<NoteIcon size={24} weight="duotone" />}
        />
  
      </section>
    );
  }