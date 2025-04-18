package com.sellular.sampledropwizard.registers.managed;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sellular.commons.kafka.connect.KafkaConsumerRegistry;
import io.dropwizard.lifecycle.Managed;
import lombok.Getter;

@Getter
@Singleton
public class ManagedResource implements Managed {

    private final DataSourceManaged dataSourceManaged;

    @Inject
    public ManagedResource(final DataSourceManaged dataSourceManaged) {
        this.dataSourceManaged = dataSourceManaged;
    }

    @Override
    public void start() throws Exception {
        dataSourceManaged.start();
        KafkaConsumerRegistry.startConsuming();
    }

    @Override
    public void stop() throws Exception {
        dataSourceManaged.stop();
        KafkaConsumerRegistry.shutdown();
    }

}
