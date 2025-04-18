package com.sellular.sampledropwizard.configuration;

import com.sellular.commons.dropwizard.configuration.BaseConfiguration;
import com.sellular.commons.kafka.config.KafkaConfiguration;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HelloWorldConfiguration extends BaseConfiguration {

    @NotNull
    private DatabaseConfig databaseConfig;

    @NotNull
    private KafkaConfiguration kafkaConfiguration;

}