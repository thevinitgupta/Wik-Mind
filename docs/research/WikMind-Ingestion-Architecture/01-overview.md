# System Overview

## Goals
- Build an extensible ingestion platform.
- Support TEXT and MARKDOWN first.
- Design processors that later support PDF, URL, Notion and YouTube.
- Decouple upload, parsing, AI enrichment and indexing.

## High Level Flow

Client
→ Source API
→ Source Entity (PENDING)
→ Job Queue
→ Parser
→ AI Enrichment
→ Knowledge Persistence
→ READY

Design principles:
- Event driven
- Idempotent jobs
- Retryable processing
- AI as a pipeline stage
- Local-first LLM
