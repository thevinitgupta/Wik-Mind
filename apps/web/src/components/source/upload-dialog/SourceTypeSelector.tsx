"use client";

import { Card } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";

import { cn } from "@/lib/utils/tw";

import { SourceType } from "@/types/source";
import { Controller, UseFormReturn } from "react-hook-form";

import { CreateSourceForm } from "@/types/schema/source.schema";

import { FileDocIcon, LinkIcon } from "@phosphor-icons/react";

interface Props {
  form: UseFormReturn<CreateSourceForm>;
}

const SourceTypeSelector = ({ form }: Props) => {
  return (
    <Controller
      control={form.control}
      name="sourceType"
      render={({ field }) => (
        <RadioGroup
          value={field.value}
          onValueChange={field.onChange}
          className="flex "
        >
          <Label htmlFor="file">
            <Card
              className={cn(
                "cursor-pointer p-5 transition-all hover:border-primary",
                field.value === SourceType.FILE && "border-primary bg-primary/5"
              )}
            >
              <div className="flex items-center gap-3">
                <RadioGroupItem value={SourceType.FILE} id="file" />

                <FileDocIcon className="h-5 w-5" />

                <div>
                  <p className="font-medium">Upload File</p>

                  <p className="text-xs text-muted-foreground">
                    PDF, DOCX, TXT...
                  </p>
                </div>
              </div>
            </Card>
          </Label>

          <Label htmlFor="url">
            <Card
              className={cn(
                "cursor-pointer p-5 transition-all hover:border-primary",
                field.value === SourceType.URL && "border-primary bg-primary/5"
              )}
            >
              <div className="flex items-center gap-3">
                <RadioGroupItem value={SourceType.URL} id="url" />

                <LinkIcon className="h-5 w-5" />

                <div>
                  <p className="font-medium">Website URL</p>

                  <p className="text-xs text-muted-foreground">
                    Import a webpage
                  </p>
                </div>
              </div>
            </Card>
          </Label>
        </RadioGroup>
      )}
    />
  );
};

export default SourceTypeSelector;
