"use client"
import { Card, CardContent } from "@/components/ui/card";
import { cn } from "@/lib/utils/tw";

import { ReactNode } from "react";

interface WorkspaceInsightCardProps {
  title: string;
  value: number;
  subtitle: string;

  icon: ReactNode;

  accentClass: string;
}

export function WorkspaceInsightCard({
  title,
  value,
  subtitle,
  icon,
  accentClass,
}: WorkspaceInsightCardProps) {
  return (
    <Card
      className={cn(
        "rounded-2xl border border-white/5 bg-card transition-all duration-300",
        "hover:border-white/10 hover:-translate-y-0.5 hover:shadow-lg"
      )}
    >
      <CardContent className="flex items-center justify-between">

        <div className="">

          <p className="text-2xs font-medium text-muted-foreground">
            {title}
          </p>

          <h3 className="text-2xl font-bold tracking-tight">
            {value}
          </h3>

          <p className="text-xs text-muted-foreground">
            {subtitle}
          </p>

        </div>

        <div
          className={cn(
            "flex h-12 w-12 items-center justify-center rounded-2xl",
            accentClass
          )}
        >
          {icon}
        </div>

      </CardContent>
    </Card>
  );
}