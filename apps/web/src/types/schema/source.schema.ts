import {z} from "zod";
import { SourceType } from "../source";

export const createSourceSchema = z
    .object({
        sourceType: z.enum(SourceType),

        multipartFile: z
            .instanceof(File)
            .optional(),

        url: z.union([
            z.url(),
            z.literal("")
        ]).optional(),

        displayName: z.string().optional()
    })
    .superRefine((data, ctx) => {

        if (data.sourceType === SourceType.FILE && !data.multipartFile) {
            ctx.addIssue({
                code: "custom",
                path: ["multipartFile"],
                message: "Select a file"
            })
        }

        if (data.sourceType === SourceType.URL && !data.url) {
            ctx.addIssue({
                code: "custom",
                path: ["url"],
                message: "Enter a URL"
            })
        }
    })

export type CreateSourceForm =
    z.infer<typeof createSourceSchema>;