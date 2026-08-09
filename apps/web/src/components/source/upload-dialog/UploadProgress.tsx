"use client";

import { Progress } from "@/components/ui/progress";

interface UploadProgressProps {
  progress: number;
}

const UploadProgress = ({ progress }: UploadProgressProps) => {
  return (
    <div className="space-y-2">
      <div className="flex items-center justify-between">
        <span className="text-sm font-medium">Uploading Source</span>

        <span className="text-sm text-muted-foreground">{progress}%</span>
      </div>

      <Progress value={progress} />
    </div>
  );
};

export default UploadProgress;
