"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Field, FieldGroup } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

import { PlusIcon } from "@phosphor-icons/react";

import { useCreateWorkspace } from "@/lib/hooks/workspace/useCreateWorkspace";
import {
  WorkspaceForm,
  workspaceSchema,
} from "@/types/schema/workspace.schema";

export function CreateWorkspace() {
  const [open, setOpen] = useState(false);

  const createWorkspace = useCreateWorkspace();

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm<WorkspaceForm>({
    resolver: zodResolver(workspaceSchema),
    defaultValues: {
      name: "",
    },
  });

  console.log(watch());

  const onSubmit = async (data: WorkspaceForm) => {
    try {
      console.log("Create workspace called:", data);
      await createWorkspace.mutateAsync(data);

      reset();

      setOpen(false);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline">
          <PlusIcon data-icon="inline-start" />
          Create New Workspace
        </Button>
      </DialogTrigger>

      <DialogContent className="sm:max-w-sm" showCloseButton={false}>
        <form
          onSubmit={handleSubmit(onSubmit, (errors) => {
            console.log("Validation errors", errors);
          })}
        >
          <DialogHeader className="mb-6">
            <DialogTitle className="mt-4">New Workspace</DialogTitle>

            <DialogDescription className="text-xs">
              Create a workspace to start dumping your knowledge.
            </DialogDescription>
          </DialogHeader>

          <FieldGroup>
            <Field>
              <Label htmlFor="name">Name</Label>

              <Input
                id="name"
                placeholder="Brain Buffet"
                {...register("name")}
              />

              {errors.name && (
                <p className="text-sm text-destructive mt-1">
                  {errors.name.message}
                </p>
              )}
            </Field>
          </FieldGroup>

          <DialogFooter className="mt-6">
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>

            <Button
              type="submit"
              disabled={createWorkspace.isPending}
              onClick={() => console.log("Submit button clicked")}
            >
              {createWorkspace.isPending ? "Creating..." : "Create"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
