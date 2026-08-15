import { z } from "zod";

export const createSourceVersionSchema = z.object({
  multipartFile: z
    .instanceof(File, {
      message: "Select a file",
    })
    .optional(),

  displayName: z.string().optional(),
})
.superRefine((data, ctx) => {
  if (!data.multipartFile) {
    ctx.addIssue({
      code: "custom",
      path: ["multipartFile"],
      message: "Select a file",
    });
  }
});

export type CreateSourceVersionForm =
  z.infer<typeof createSourceVersionSchema>;