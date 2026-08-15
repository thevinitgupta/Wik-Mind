package com.wikmind.service.source.utils;

import com.wikmind.service.source.entity.dto.CreateSourceRequest;
import com.wikmind.service.source.entity.enums.SourceType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SourceNameUtil {
    public String fetchNameFromCreationRequest(CreateSourceRequest createSourceRequest){
        if(!createSourceRequest.displayName().isEmpty()) {
            return createSourceRequest.displayName();
        }

        if(createSourceRequest.sourceType().equals(SourceType.FILE)){
            return createSourceRequest.multipartFile().getOriginalFilename()==null ? createSourceRequest.multipartFile().getName() : createSourceRequest.multipartFile().getOriginalFilename();
        }
        return generateUniqueFileName();
    }

    private static String generateUniqueFileName()
    {
        UUID uuid = UUID.randomUUID();
        return "file_" + uuid.toString();
    }
}
