# Job Queue

Suggested stack:
- Spring Boot
- PostgreSQL job table (initial)
- @Scheduled dispatcher
- @Async workers

Future:
RabbitMQ / Kafka if scale requires.

Job stages:
NORMALIZE
PARSE
AI_SUMMARIZE
TOPIC_EXTRACTION
CLAIM_EXTRACTION
PERSIST
COMPLETE
