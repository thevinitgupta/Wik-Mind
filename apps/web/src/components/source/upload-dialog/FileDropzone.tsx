"use client";

import { Controller, UseFormReturn } from "react-hook-form";

import { CloudArrowUpIcon, FileTxtIcon, XIcon } from "@phosphor-icons/react";

import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

import { CreateSourceForm } from "@/types/schema/source.schema";

interface FileDropzoneProps {
  form: UseFormReturn<CreateSourceForm>;
  disabled: boolean;
}

const FileDropzone = ({ form, disabled }: FileDropzoneProps) => {
  const formatFileSize = (bytes: number) => {
    if (bytes < 1024) return `${bytes} B`;

    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;

    if (bytes < 1024 * 1024 * 1024)
      return `${(bytes / 1024 / 1024).toFixed(1)} MB`;

    return `${(bytes / 1024 / 1024 / 1024).toFixed(1)} GB`;
  };

  return (
    <Controller
      control={form.control}
      name="multipartFile"
      disabled={disabled}
      render={({ field, fieldState }) => {
        const file = field.value;

        const onFileSelected = (selectedFile?: File) => {
          if (!selectedFile) return;

          form.setValue("multipartFile", selectedFile, {
            shouldDirty: true,
            shouldTouch: true,
            shouldValidate: true,
          });
        };

        return (
          <div className="space-y-3">
            <Card
              className="
                                border-dashed
                                transition-colors
                                hover:border-primary
                            "
            >
              <label
                htmlFor="source-upload"
                className="
                                    flex
                                    cursor-pointer
                                    flex-col
                                    items-center
                                    justify-center
                                    gap-4
                                    p-10
                                    text-center
                                "
                onDragOver={(e) => e.preventDefault()}
                onDrop={(e) => {
                  e.preventDefault();

                  const dropped = e.dataTransfer.files?.[0];

                  onFileSelected(dropped);
                }}
              >
                <CloudArrowUpIcon
                  className="
                                        h-12
                                        w-12
                                        text-muted-foreground
                                    "
                />

                <div>
                  <p className="font-medium">Drop your file here</p>

                  <p className="text-sm text-muted-foreground mt-1">
                    or click to browse
                  </p>
                </div>

                <Button type="button" variant="secondary">
                  Browse Files
                </Button>

                <p className="text-xs text-muted-foreground">
                  PDF • DOCX • TXT • Markdown • Images
                </p>

                <input
                  id="source-upload"
                  type="file"
                  hidden
                  onChange={(e) => onFileSelected(e.target.files?.[0])}
                />
              </label>
            </Card>

            {file && (
              <Card className="p-4">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <FileTxtIcon
                      className="h-8 w-8 text-primary"
                    />

                    <div>
                      <p className="font-medium">{file.name}</p>

                      <p className="text-sm text-muted-foreground">
                        {formatFileSize(file.size)}
                      </p>
                    </div>
                  </div>

                  <Button
                    type="button"
                    variant="ghost"
                    size="icon"
                    onClick={() =>
                      form.setValue("multipartFile", undefined, {
                        shouldDirty: true,
                        shouldTouch: true,
                        shouldValidate: true,
                      })
                    }
                  >
                    <XIcon className="h-4 w-4" />
                  </Button>
                </div>
              </Card>
            )}

            {fieldState.error && (
              <p className="text-sm text-destructive">
                {fieldState.error.message}
              </p>
            )}
          </div>
        );
      }}
    />
  );
};

export default FileDropzone;
