# AI Integration

AI is a processing stage.

Prompt outputs structured JSON:

{
 title,
 summary,
 topics[],
 keywords[],
 claims[]
}

Persist each object independently.

Never couple parsing with embeddings.

Use schema validation before persistence.
