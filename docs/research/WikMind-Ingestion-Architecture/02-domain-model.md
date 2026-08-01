# Domain Model

## Entities

Workspace
- id
- ...

Source
- id
- workspaceId
- title
- type
- status
- originalContent
- normalizedContent
- parsedContent
- summary
- processingError
- createdAt
- processedAt

SourceProcessingJob
- id
- sourceId
- stage
- status
- attempts
- startedAt
- completedAt
- worker

Topic
- id
- workspaceId
- name

Claim
- id
- workspaceId
- sourceId
- subject
- predicate
- object
- confidence

Keyword
- id
- workspaceId
- value

Future:
Entity
Relationship
Embedding
Conversation
Chunk
