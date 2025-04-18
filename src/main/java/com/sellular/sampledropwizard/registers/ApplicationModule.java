package com.sellular.sampledropwizard.registers;

import com.google.inject.AbstractModule;
import com.sellular.commons.jpa.config.GuiceJpaModule;
import com.sellular.commons.jpa.config.HikariDatabaseConfig;
import com.sellular.commons.jpa.config.InstrumentedHikariDataSource;
import com.sellular.commons.kafka.config.KafkaConfiguration;
import com.sellular.commons.kafka.connect.KafkaProducerClient;
import com.sellular.sampledropwizard.configuration.HelloWorldConfiguration;
import io.dropwizard.core.setup.Environment;
import org.modelmapper.ModelMapper;

import javax.sql.DataSource;

public class ApplicationModule extends AbstractModule {

    final HelloWorldConfiguration configuration;

    final Environment environment;

    public ApplicationModule(final HelloWorldConfiguration configuration, final Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(HelloWorldConfiguration.class).toInstance(configuration);
        bind(Environment.class).toInstance(environment);
        bind(ModelMapper.class).toInstance(new ModelMapper());
        final KafkaProducerClient kafkaProducerClient = new KafkaProducerClient(configuration.getKafkaConfiguration());
        bind(KafkaProducerClient.class).toInstance(kafkaProducerClient);
        bind(KafkaConfiguration.class).toInstance(configuration.getKafkaConfiguration());

        // Bind the DataSource
        final HikariDatabaseConfig dbConfig = configuration.getDatabaseConfig();
        final InstrumentedHikariDataSource instrumentedDataSource = new InstrumentedHikariDataSource(environment, dbConfig);
        environment.lifecycle().manage(instrumentedDataSource);
        bind(DataSource.class).toInstance(instrumentedDataSource.getHikariDataSource());
        install(new GuiceJpaModule(instrumentedDataSource.getHikariDataSource(),
                configuration.getDatabaseConfig().getPersistenceUnitName(),
                configuration.getDatabaseConfig().getJpaPropertiesMap()));
    }

}
