package com.wikmind.service.source.entity.enums;

public enum ProcessingStage {
    EXTRACTION,
    CHUNKING,
    EMBEDDING,
    KNOWLEDGE_GRAPH,
    INDEXING;

    private static final ProcessingStage[] VALS = values();

    /**
     * Returns the next stage in Processing:
     * EXTRACTION -> CHUNKING -> EMBEDDING -> KNOWLEDGE_GRAPH -> INDEXING
     * @return ProcessingStage
     */
    public ProcessingStage next() {
        return VALS[(this.ordinal() + 1) % VALS.length];
    }
}
