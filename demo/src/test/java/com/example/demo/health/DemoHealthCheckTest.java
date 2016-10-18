package com.example.demo.health;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DemoHealthCheckTest {
    @Test
    public void testToggleHealth() throws Exception {
        final DemoHealthCheck healthCheck = new DemoHealthCheck();

        assertTrue(healthCheck.check().isHealthy());
        healthCheck.toggleHealth();
        assertFalse(healthCheck.check().isHealthy());
    }
}
