# 🧠 WIK Mind

> **AI-powered Knowledge Intelligence Platform**\
> Transform documents into an explorable network of knowledge with
> AI-powered summarization, claim extraction, semantic search, and
> knowledge graphs.


![Status](https://img.shields.io/badge/status-active-success)
![License](https://img.shields.io/badge/license-MIT-blue)
![Java](https://img.shields.io/badge/Java-21-orange) ![Spring
Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![Next.js](https://img.shields.io/badge/Next.js-15-black)


------------------------------------------------------------------------

## ✨ Features

-   📄 Document ingestion (PDF, Markdown, Web)
-   🤖 AI-powered summarization
-   🧩 Claim, Topic & Entity extraction
-   🔗 Relationship detection
-   🕸️ Interactive knowledge graph
-   🔍 Semantic search
-   📚 Source citations & evidence tracking
-   ⚖️ Contradiction detection *(planned)*
-   👥 Multi-workspace collaboration *(planned)*

------------------------------------------------------------------------

## 🏗️ Tech Stack

  Layer      Technology
  ---------- -------------------------------------------------------
  Frontend   Next.js, React, TanStack Query, Zustand, Tailwind CSS
  Backend    Spring Boot, Spring AI, Spring Security
  Database   PostgreSQL
  Cache      Redis
  Future     pgvector, Neo4j, Ollama, Elasticsearch

------------------------------------------------------------------------

## 📂 Repository Structure

``` text
apps/
  web/

backend/
  api/

libraries/
  prompts/
  shared/

infrastructure/
  docker/
  postgres/
  redis/

docs/
scripts/
```

------------------------------------------------------------------------

## 📖 Documentation

**Architecture**
-   docs/architecture/system-overview.md
-   docs/architecture/project-structure.md
-   docs/architecture/backend-architecture.md
-   docs/architecture/frontend-architecture.md
-   docs/architecture/database-design.md
-   docs/architecture/knowledge-model.md
-   docs/architecture/ai-pipelines.md
-   docs/architecture/deployment.md

**Development**

-   docs/development/coding-guidelines.md
-   docs/roadmap/roadmap.md
-   docs/adr/


------------------------------------------------------------------------

## 🚀 Getting Started

### Prerequisites

-   Java 21
-   Node.js 22+
-   Docker Desktop
-   PostgreSQL (or Docker)

### Clone

``` bash
git clone https://github.com/<your-org>/wik-mind.git
cd wik-mind
```

### Run

``` bash
docker compose up --build
```

------------------------------------------------------------------------

## 🛣️ Roadmap

-   ⏳ MVP: Authentication, Workspaces, Summarization
-   ⏳ Claims & Relations
-   ⏳ Embeddings + Semantic Search
-   ⏳ Knowledge Graph
-   ⏳ AI Agents
-   ⏳ Collaboration

------------------------------------------------------------------------

## 🤝 Contributing

Not Open 

------------------------------------------------------------------------

## 📜 License

MIT License
