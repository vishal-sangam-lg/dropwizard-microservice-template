package com.sellular.sampledropwizard.service.kafka.consumer;

import com.google.inject.Inject;
import com.sellular.commons.kafka.connect.KafkaConsumerClient;
import com.sellular.commons.kafka.config.KafkaConfiguration;
import com.sellular.sampledropwizard.constants.KafkaEvents;
import com.sellular.user.v1.UserEventModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserCreatedConsumer extends KafkaConsumerClient<String, UserEventModel> {

    @Inject
    public UserCreatedConsumer(final KafkaConfiguration kafkaConfiguration) {
        super(kafkaConfiguration, KafkaEvents.user_created.name());
    }

    @Override
    public void process(String key, UserEventModel event) {
        log.info("Processing UserCreated event key: {}", key);
        log.info("Processing UserCreated event: username={}, email={}, contact={}",
                event.getData().getUsername(), event.getData().getEmail(), event.getData().getContact());

        // business logic goes here
        // e.g., trigger notifications, etc.
    }

}
