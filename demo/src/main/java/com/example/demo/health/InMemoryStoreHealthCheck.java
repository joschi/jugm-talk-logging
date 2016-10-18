package com.example.demo.health;

import com.codahale.metrics.health.HealthCheck;
import com.example.demo.InMemoryStore;

import static java.util.Objects.requireNonNull;

public class InMemoryStoreHealthCheck extends HealthCheck {
    private final InMemoryStore store;

    public InMemoryStoreHealthCheck(final InMemoryStore store) {
        this.store = requireNonNull(store);
    }

    @Override
    protected Result check() throws Exception {
        if (store.isRunning()) {
            return Result.healthy("Store is OK.");
        } else {
            return Result.unhealthy("Store is offline.");
        }
    }

}
