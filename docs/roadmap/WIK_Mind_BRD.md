# WIK Mind

## Business Requirements Document (BRD)

## 1. Formal Project Definition

LLM Wiki is a local-first AI-powered knowledge management platform that
transforms unstructured documents into a structured knowledge graph and
Markdown wiki. Instead of answering directly from uploaded documents
like traditional RAG, it compiles reusable knowledge through summaries,
entities, concepts, claims, relationships and contradictions.

Core goals: - Local-first - Markdown as source of truth - Pluggable LLM
providers - Search + Chat + Mind Map

------------------------------------------------------------------------

## 2. Feature Breakdown

  Feature                   Purpose                            Integrates With
  ------------------------- ---------------------------------- ----------------------
  Project Management        Creates isolated workspaces        All modules
  Source Upload             Accepts PDFs/Markdown/Text         Ingestion
  Text Extraction           Converts files to text             Summary & Extraction
  Summary Generation        Produces concise summaries         Wiki
  Entity Extraction         Finds important things             Graph
  Concept Extraction        Finds ideas                        Graph
  Claim Extraction          Finds factual assertions           Contradictions
  Knowledge Graph           Stores nodes & edges               Search & Mind Map
  Wiki Generator            Produces Markdown pages            Chat
  Contradiction Detection   Compares claims                    Graph
  Hybrid Search             Keyword + semantic search          Chat
  Mind Map                  Visual exploration                 Graph
  Chat                      Answers using compiled knowledge   Search

------------------------------------------------------------------------

## 3. Data Flow

Ingestion:

Create Project → Upload Source → Extract Text → Generate Summary →
Extract Entities → Extract Concepts → Extract Claims → Build Knowledge
Graph → Generate Wiki → Generate Embeddings

Query:

Question → Hybrid Search → Graph Expansion → Context Assembly → LLM →
Answer + Citations

------------------------------------------------------------------------

## 4. Architecture

NextJS UI ↓ Spring Boot API ↓ Modules: - Project - Source - Ingestion -
Graph - Wiki - Search - Chat - AI Provider

Persistence: - Markdown Wiki - SQLite Metadata - Vector Store (future)

AI Providers: - Ollama - OpenAI - Gemini

------------------------------------------------------------------------

## 5. Component Breakdown

Frontend: - Dashboard - Upload - Wiki Explorer - React Flow Mind Map -
Search - Chat - Settings

Backend: - Project Module - Source Module - Ingestion Module - Summary
Generator - Entity Extractor - Concept Extractor - Claim Extractor -
Graph Builder - Contradiction Engine - Search Engine - Chat Engine - AI
Provider Abstraction

------------------------------------------------------------------------

## 6. Design Principles

-   Local-first
-   Graph-first
-   Markdown-first
-   Provider agnostic
-   Explainable through citations

------------------------------------------------------------------------

## 7. MVP

-   Projects
-   Upload
-   Summary
-   Entity
-   Concept
-   Claim
-   Graph
-   Contradictions
-   Mind Map
-   Search
-   Chat

------------------------------------------------------------------------

## 8. Roadmap

V0.2 Knowledge Quality V0.3 PKM V0.4 Research Assistant V0.5 Advanced
Search V0.6 Desktop V1.0 Knowledge Operating System
