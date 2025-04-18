package com.sellular.sampledropwizard.constants;

import com.sellular.commons.kafka.constants.KafkaEventInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KafkaEvents implements KafkaEventInterface {

    user_created("user_created", "com.sellular.user.user_created");

    private final String eventType;
    private final String topic;

}