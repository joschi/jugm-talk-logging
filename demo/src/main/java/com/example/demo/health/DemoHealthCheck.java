package com.example.demo.health;

import com.codahale.metrics.health.HealthCheck;

public class DemoHealthCheck extends HealthCheck {
    private volatile boolean healthy = true;

    public void toggleHealth() {
        healthy = !healthy;
    }

    @Override
    protected Result check() throws Exception {
        if (healthy) {
            return Result.healthy("Everything is fine. :)");
        } else {
            return Result.unhealthy("Everything is on fire. :(");
        }
    }
}
