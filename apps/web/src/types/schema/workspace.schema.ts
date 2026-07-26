import { z } from "zod";

export const workspaceSchema = z.object({
    name: z
      .string()
      .trim()
      .min(3, "Workspace name is too short")
      .max(60),
  });
  
  export type WorkspaceForm = z.infer<typeof workspaceSchema>;
  