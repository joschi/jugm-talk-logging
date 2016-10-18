package com.example.demo.tasks;

import com.example.demo.health.DemoHealthCheck;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.PrintWriter;

import static java.util.Objects.requireNonNull;

public class ToggleHealthCheckTask extends Task {
    private static final Logger log = LoggerFactory.getLogger(ToggleHealthCheckTask.class);

    private final DemoHealthCheck healthCheck;

    public ToggleHealthCheckTask(String name, DemoHealthCheck healthCheck) {
        super(name);
        this.healthCheck = requireNonNull(healthCheck);
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        healthCheck.toggleHealth();
        log.info("Health state toggled.");
        output.println("Health state toggled.");
    }
}
