package com.wikmind.service.source.service.registry;

import com.wikmind.service.source.entity.enums.SourceType;
import com.wikmind.service.source.service.upload.SourceUploadStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SourceUploadStrategyRegistry {
    private final Map<SourceType, SourceUploadStrategy> strategies;

    public SourceUploadStrategyRegistry(List<SourceUploadStrategy> strategyList) {

        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        SourceUploadStrategy::supports,
                        Function.identity()
                ));
    }

    public SourceUploadStrategy get(SourceType type) {

        SourceUploadStrategy strategy = strategies.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "No upload strategy registered for " + type
            );
        }

        return strategy;
    }
}
