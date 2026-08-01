# Ingestion Pipeline

1. Accept upload
2. Persist Source (PENDING)
3. Create Processing Job
4. Queue Job
5. Worker picks job
6. Normalize
7. Parse
8. AI Enrichment
9. Persist Topics / Claims / Keywords
10. READY

Failure path:
FAILED
Retry with exponential backoff.
