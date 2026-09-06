package com.wikmind.service.common.config;

import com.wikmind.service.common.entity.dto.ProcessingJobQueuedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, ProcessingJobQueuedEvent>
    processingJobProducerFactory(KafkaProperties kafkaProperties) {

        Map<String, Object> properties =
                new HashMap<>(kafkaProperties.buildProducerProperties());

        properties.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );

        properties.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JacksonJsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, ProcessingJobQueuedEvent>
    processingJobKafkaTemplate(
            // multiple factory and template beans can be defined
            @Qualifier("processingJobProducerFactory")
            ProducerFactory<String, ProcessingJobQueuedEvent> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

}
