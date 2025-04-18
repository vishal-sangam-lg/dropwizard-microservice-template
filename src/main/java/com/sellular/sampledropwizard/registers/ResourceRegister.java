package com.sellular.sampledropwizard.registers;

import com.google.inject.Injector;
import com.sellular.commons.kafka.connect.KafkaConsumerRegistry;
import com.sellular.sampledropwizard.resource.UserResource;
import com.sellular.sampledropwizard.service.kafka.consumer.UserCreatedConsumer;
import io.dropwizard.core.setup.Environment;

public class ResourceRegister {

    private final Injector injector;

    private final Environment environment;

    public ResourceRegister(final Injector injector) {
        this.injector = injector;
        this.environment = injector.getInstance(Environment.class);
    }

    public void register() {
        registerResources();
        registerKafkaConsumers();
    }

    private void registerResources() {
        environment.jersey().register(injector.getInstance(UserResource.class));
    }

    private void registerKafkaConsumers() {
        KafkaConsumerRegistry.registerConsumer(injector.getInstance(UserCreatedConsumer.class));
    }

}
