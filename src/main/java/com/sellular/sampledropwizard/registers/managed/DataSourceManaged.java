package com.sellular.sampledropwizard.registers.managed;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import io.dropwizard.lifecycle.Managed;

@Singleton
public class DataSourceManaged implements Managed {

    private final PersistService persistService;

    @Inject
    public DataSourceManaged(final PersistService persistService) {
        this.persistService = persistService;
    }

    @Override
    public void start() throws Exception {
        persistService.start();
    }

    @Override
    public void stop() {
        persistService.stop();
    }

}
