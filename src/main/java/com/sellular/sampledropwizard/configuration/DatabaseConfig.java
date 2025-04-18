package com.sellular.sampledropwizard.configuration;

import com.sellular.commons.jpa.config.HikariDatabaseConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DatabaseConfig extends HikariDatabaseConfig {

    private Map<String, Object> jpaPropertiesMap;

    private String persistenceXmlLocation;

    private String persistenceUnitName;

}