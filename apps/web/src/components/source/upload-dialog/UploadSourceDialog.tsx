"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { useUploadSource } from "@/lib/hooks/source/useUploadSource";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

import { Button } from "@/components/ui/button";

import {
  CreateSourceForm,
  createSourceSchema,
} from "@/types/schema/source.schema";

import { SourceType } from "@/types/source";

import SourceTypeSelector from "./SourceTypeSelector";
import FileDropzone from "./FileDropzone";
import UploadProgress from "./UploadProgress";
import { Field, FieldLabel, FieldDescription } from "@/components/ui/field";
import { Input } from "@/components/ui/input";

interface UploadSourceDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  workspaceId: string;
}

const UploadSourceDialog = ({
  open,
  onOpenChange,
  workspaceId,
}: UploadSourceDialogProps) => {
  const form = useForm<CreateSourceForm>({
    resolver: zodResolver(createSourceSchema),
    mode: "onChange",
    reValidateMode: "onChange",

    defaultValues: {
      sourceType: SourceType.FILE,
      displayName: "",
      multipartFile: undefined,
      url: "",
    },
  });

  console.log({
    isValid: form.formState.isValid,
    isDirty: form.formState.isDirty,
    touched: form.formState.touchedFields,
    values: form.getValues(),
  });

  const mutation = useUploadSource(workspaceId);

  const sourceType = form.watch("sourceType");

  const onSubmit = (values: CreateSourceForm) => {
    mutation.mutate(values, {
      onSuccess: () => {
        form.reset({
          sourceType: SourceType.FILE,
          displayName: "",
          multipartFile: undefined,
          url: "",
        });

        onOpenChange(false);
      },
    });
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-xl">
        <DialogHeader>
          <DialogTitle>Add Knowledge Source</DialogTitle>

          <DialogDescription>
            Upload files or import content from a URL.
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
          <fieldset disabled={mutation.isPending} className="space-y-6">
            <SourceTypeSelector form={form} />

            {sourceType === SourceType.FILE && (
              <FileDropzone
                form={form}
                name="multipartFile"
                disabled={mutation.isPending}
              />
            )}

            {sourceType === SourceType.URL && (
              <input
                {...form.register("url")}
                className="w-full rounded-md border px-3 py-2"
                placeholder="https://..."
              />
            )}

            <Field>
              <FieldLabel htmlFor="displayName">Filename</FieldLabel>
              <Input
                {...form.register("displayName")}
                type="text"
                placeholder="Enter the file name"
              />
              <FieldDescription>
                Choose a display name for the file(Optional).
              </FieldDescription>
            </Field>
          </fieldset>

          {mutation.isPending && (
            <UploadProgress progress={mutation.progress} />
          )}

          {mutation.isError && (
            <p className="text-sm text-destructive">{mutation.errorMessage}</p>
          )}

          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              disabled={mutation.isPending}
              onClick={() => onOpenChange(false)}
            >
              Cancel
            </Button>

            <Button
              type="submit"
              disabled={mutation.isPending || !form.formState.isValid}
            >
              {mutation.isPending ? "Uploading..." : "Upload Source"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default UploadSourceDialog;
