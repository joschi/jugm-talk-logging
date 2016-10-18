package com.example.demo.tasks;

import com.example.demo.health.DemoHealthCheck;
import com.google.common.collect.ImmutableMultimap;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assume.assumeTrue;

public class ToggleHealthCheckTaskTest {
    @Test
    public void testToggle() throws Exception {
        final DemoHealthCheck healthCheck = new DemoHealthCheck();
        assumeTrue(healthCheck.execute().isHealthy());

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintWriter writer = new PrintWriter(out, true);
        final ToggleHealthCheckTask task = new ToggleHealthCheckTask("toggle-task", healthCheck);

        task.execute(ImmutableMultimap.of(), writer);

        assertEquals("Health state toggled.\n", new String(out.toByteArray(), StandardCharsets.UTF_8));
        assertFalse(healthCheck.execute().isHealthy());
    }
}
