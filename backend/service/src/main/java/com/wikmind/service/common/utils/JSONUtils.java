package com.wikmind.service.common.utils;

import com.wikmind.service.common.exceptions.EntityPayloadGenerationException;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class JSONUtils {
    private final ObjectMapper objectMapper;

    public JSONUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T>String entityToJSON(T entity){
        if(entity==null) return null;
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JacksonException e) {
            throw new EntityPayloadGenerationException("Error generating JSON Payload from entity:"+entity, e);
        }
    }

    public <T> T jsonToEntity(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JacksonException e) {
            throw new RuntimeException("Error parsing JSON into entity: " + clazz.getSimpleName(), e);
        }
    }
}

