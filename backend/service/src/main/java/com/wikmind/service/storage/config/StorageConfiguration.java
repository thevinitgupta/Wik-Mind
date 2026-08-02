package com.wikmind.service.storage.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(StorageProperties.class)
@Configuration
public class StorageConfiguration {
}
