package com.example.demo;

import com.example.demo.auth.DemoAuthenticator;
import com.example.demo.auth.DemoAuthorizer;
import com.example.demo.auth.User;
import com.example.demo.health.DemoHealthCheck;
import com.example.demo.health.InMemoryStoreHealthCheck;
import com.example.demo.jersey.RequestIdFilter;
import com.example.demo.model.ImmutableKitten;
import com.example.demo.model.Kitten;
import com.example.demo.resource.KittenResource;
import com.example.demo.resource.StatusResource;
import com.example.demo.tasks.ToggleHealthCheckTask;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import net.gini.dropwizard.gelf.filters.GelfLoggingFilter;
import net.gini.dropwizard.gelf.logging.GelfBootstrap;
import net.gini.dropwizard.gelf.logging.UncaughtExceptionHandlers;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.graylog.metrics.GelfReporter;

import javax.servlet.DispatcherType;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class DemoApplication extends Application<DemoConfiguration> {
    private static final String GELF_HOST = "127.0.0.1";
    private static final int GELF_PORT = 12201;
    private static final String NAME = DemoApplication.class.getSimpleName();

    public static void main(final String[] args) throws Exception {
        // Send startup messages to Graylog
        GelfBootstrap.bootstrap(NAME, GELF_HOST, GELF_PORT, false);
        Thread.currentThread().setUncaughtExceptionHandler(
                UncaughtExceptionHandlers.loggingSystemExitBuilder(NAME, GELF_HOST)
                        .port(GELF_PORT)
                        .build());

        new DemoApplication().run(args);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void initialize(final Bootstrap<DemoConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<DemoConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DemoConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
    }

    @Override
    public void run(final DemoConfiguration configuration, final Environment environment) throws Exception {
        final InMemoryStore store = new InMemoryStore();
        environment.lifecycle().manage(store);

        // Add Jersey filter to add request ID on every request
        environment.jersey().register(RequestIdFilter.class);

        // Set-up authentication
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new DemoAuthenticator())
                        .setAuthorizer(new DemoAuthorizer("jochen"))
                        .setRealm("Secret")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        // Set-up JAX-RS resources
        environment.jersey().register(new StatusResource());
        environment.jersey().register(new KittenResource(store));

        // Set-up health checks
        final DemoHealthCheck demoHealthCheck = new DemoHealthCheck();
        environment.healthChecks().register("demo-health", demoHealthCheck);
        environment.healthChecks().register("store-health", new InMemoryStoreHealthCheck(store));

        environment.admin().addTask(new ToggleHealthCheckTask("toggle-health", demoHealthCheck));

        // add one demo kitten
        final Kitten foo = ImmutableKitten.builder()
                .id(0)
                .name("Findus")
                .type(Kitten.KittenType.CUTE)
                .build();
        store.put(foo);

        // Send request logs to Graylog
        environment.servlets()
                .addFilter("request-logs", new GelfLoggingFilter())
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // Report metrics to Graylog
        final GelfReporter gelfReporter = GelfReporter.forRegistry(environment.metrics())
                .host(new InetSocketAddress("127.0.0.1", 12201))
                .source("demo.example.com")
                .additionalFields(Collections.singletonMap("facility", "metrics"))
                .build();

        gelfReporter.start(30, TimeUnit.SECONDS);
    }

}
