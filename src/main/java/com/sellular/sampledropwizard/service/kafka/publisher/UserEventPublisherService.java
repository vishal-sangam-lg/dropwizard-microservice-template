package com.sellular.sampledropwizard.service.kafka.publisher;

import com.google.inject.Inject;
import com.sellular.commons.kafka.connect.KafkaProducerClient;
import com.sellular.sampledropwizard.utils.ProtoModelMapper;
import com.sellular.user.v1.UserEventModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserEventPublisherService {

    final private KafkaProducerClient kafkaProducerClient;

    @Inject
    public UserEventPublisherService(final KafkaProducerClient kafkaProducerClient) {
        this.kafkaProducerClient = kafkaProducerClient;
    }

    public void publishUserCreatedEvent(final String username, final String contact, final String email, final String userExternalId) {
        log.info("Publishing user_created_event with following details: {} {} {} {}", username, contact, email, userExternalId);
        UserEventModel userEventModel = ProtoModelMapper.createUserEventModel(username, contact, email, userExternalId);
        log.info("Publishing UserEventModel: {}", userEventModel);
        kafkaProducerClient.publish(userEventModel, userExternalId, userEventModel.getEventType());
    }

}
