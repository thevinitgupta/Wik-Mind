"use client";

import { Controller, FieldValues, Path, UseFormReturn } from "react-hook-form";

import { CloudArrowUpIcon, FileTxtIcon, XIcon } from "@phosphor-icons/react";

import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

interface FileDropzoneProps<T extends FieldValues> {
  form: UseFormReturn<T>;
  name: Path<T>;
  disabled: boolean;
}

const FileDropzone = <T extends FieldValues>({
  form,
  name,
  disabled,
}: FileDropzoneProps<T>) => {
  const formatFileSize = (bytes: number) => {
    if (bytes < 1024) return `${bytes} B`;

    if (bytes < 1024 * 1024) {
      return `${(bytes / 1024).toFixed(1)} KB`;
    }

    if (bytes < 1024 * 1024 * 1024) {
      return `${(bytes / 1024 / 1024).toFixed(1)} MB`;
    }

    return `${(bytes / 1024 / 1024 / 1024).toFixed(1)} GB`;
  };

  const inputId = `source-upload-${name}`;

  return (
    <Controller
      control={form.control}
      name={name}
      disabled={disabled}
      render={({ field, fieldState }) => {
        const file = field.value as File | undefined;

        const onFileSelected = (selectedFile?: File) => {
          if (!selectedFile) return;

          form.setValue(name, selectedFile as any, {
            shouldDirty: true,
            shouldTouch: true,
            shouldValidate: true,
          });
        };

        const removeFile = () => {
          form.setValue(name, undefined as any, {
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
                htmlFor={inputId}
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

                  <p className="mt-1 text-sm text-muted-foreground">
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
                  id={inputId}
                  type="file"
                  hidden
                  disabled={disabled}
                  onChange={(e) => onFileSelected(e.target.files?.[0])}
                />
              </label>
            </Card>

            {file && (
              <Card className="p-4">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <FileTxtIcon className="h-8 w-8 text-primary" />

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
                    disabled={disabled}
                    onClick={removeFile}
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
