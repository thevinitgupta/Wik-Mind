"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

import { Button } from "@/components/ui/button";

import { Field, FieldDescription, FieldLabel } from "@/components/ui/field";

import { Input } from "@/components/ui/input";

import { useUploadSourceVersion } from "@/lib/hooks/source/useUploadSourceVersion";

import FileDropzone from "./FileDropzone";
import UploadProgress from "./UploadProgress";
import {
  CreateSourceVersionForm,
  createSourceVersionSchema,
} from "@/types/schema/create-version.scheme";

interface AddSourceVersionDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  workspaceId: string;
  sourceId: string;
  sourceName: string;
}

const AddSourceVersionDialog = ({
  open,
  onOpenChange,
  workspaceId,
  sourceId,
  sourceName,
}: AddSourceVersionDialogProps) => {
  const form = useForm<CreateSourceVersionForm>({
    resolver: zodResolver(createSourceVersionSchema),

    mode: "onChange",

    reValidateMode: "onChange",

    defaultValues: {
      multipartFile: undefined,
      displayName: "",
    },
  });

  const mutation = useUploadSourceVersion(workspaceId, sourceId);
  console.log("Mutation : ",mutation.error?.message)
  console.log("Mutation Error : ",mutation.error)

  const onSubmit = (values: CreateSourceVersionForm) => {
    mutation.mutate(values, {
      onSuccess: () => {
        form.reset({
          multipartFile: undefined,
          displayName: "",
        });

        onOpenChange(false);
      },
    });
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-xl">
        <DialogHeader>
          <DialogTitle>Add New Version</DialogTitle>

          <DialogDescription>
            Upload a new version of{" "}
            <span className="font-medium text-foreground">{sourceName}</span>.
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
          <fieldset disabled={mutation.isPending} className="space-y-6">
            <FileDropzone
              form={form}
              name="multipartFile"
              disabled={mutation.isPending}
            />

            <Field>
              <FieldLabel htmlFor="displayName">Display Name</FieldLabel>

              <Input
                {...form.register("displayName")}
                type="text"
                placeholder="Enter a display name"
              />

              <FieldDescription>
                Optional. Leave empty to use the uploaded filename.
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
              {mutation.isPending ? "Uploading..." : "Add New Version"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default AddSourceVersionDialog;
