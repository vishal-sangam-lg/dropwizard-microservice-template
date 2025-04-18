package com.sellular.sampledropwizard;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sellular.commons.dropwizard.BaseApplication;
import com.sellular.sampledropwizard.configuration.HelloWorldConfiguration;
import com.sellular.sampledropwizard.registers.ApplicationModule;
import com.sellular.sampledropwizard.registers.ResourceRegister;
import com.sellular.sampledropwizard.registers.managed.ManagedResource;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class HelloWorldApplication extends BaseApplication<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(final HelloWorldConfiguration configuration, final Environment environment) throws Exception {
        super.run(configuration, environment);
        final Injector injector = Guice.createInjector(new ApplicationModule(configuration, environment));
        new ResourceRegister(injector).register();
        environment.lifecycle().manage(injector.getInstance(ManagedResource.class));
    }
}
