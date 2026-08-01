# Local LLM Strategy

Priority:
1. Ollama
2. llama.cpp
3. Remote providers (fallback)

Suggested Spring AI integration:

Worker
 -> Spring AI ChatClient
 -> Ollama (localhost)
 -> JSON response
 -> Validation
 -> Persist

Models:
- qwen3
- mistral
- llama3
- gemma

Queue workers isolate slow inference from API requests.

Future:
Dedicated AI worker service.
