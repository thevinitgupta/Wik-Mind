package com.wikmind.service.users.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuth2MapperRegistry {
    private final Map<String, OAuth2Mapper> mappers;

    public OAuth2MapperRegistry(List<OAuth2Mapper> mapperList) {
        this.mappers = mapperList.stream()
                .collect(Collectors.toMap(
                        OAuth2Mapper::getProvider,
                        Function.identity()
                ));
    }

    public OAuth2Mapper get(String provider){
        OAuth2Mapper mapper = mappers.get(provider);

        if(Objects.isNull(mapper)){
            throw new IllegalArgumentException("Unsupported Provider: "+provider);
        }
        return mapper;
    }
}
