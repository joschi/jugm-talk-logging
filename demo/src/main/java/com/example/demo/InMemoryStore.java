package com.example.demo;

import com.example.demo.model.Kitten;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class InMemoryStore implements Managed {
    private static final Logger log = LoggerFactory.getLogger(InMemoryStore.class);

    final ConcurrentMap<String, Kitten> internalStore = new ConcurrentHashMap<>();
    private AtomicBoolean running = new AtomicBoolean(false);

    public Optional<Kitten> get(final String name) {
        return Optional.ofNullable(internalStore.get(name));
    }

    public void put(final Kitten kitten) {
        internalStore.put(kitten.name(), kitten);
    }

    public boolean delete(final String name) {
        return internalStore.remove(name) != null;
    }

    public Kitten fail() {
        log.info("Running failing operation");
        throw new IllegalStateException("BOOM!");
    }

    @Override
    public void start() throws Exception {
        log.info("Starting in-memory story");
        running.set(true);
        log.info("Started in-memory story");
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopping in-memory story");
        running.set(false);
        log.info("Stopped in-memory story");
    }

    public boolean isRunning() {
        return running.get();
    }

    public Collection<Kitten> getAll() {
        return internalStore.values();
    }
}
