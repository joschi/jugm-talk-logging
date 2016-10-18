package com.example.demo.health;

import com.example.demo.InMemoryStore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InMemoryStoreHealthCheckTest {
    @Test
    public void testCheckHealthy() throws Exception {
        final InMemoryStore store = new InMemoryStoreMock(true);
        final InMemoryStoreHealthCheck healthCheck = new InMemoryStoreHealthCheck(store);

        assertTrue(healthCheck.check().isHealthy());
    }

    @Test
    public void testCheckUnHealthy() throws Exception {
        final InMemoryStore store = new InMemoryStoreMock(false);
        final InMemoryStoreHealthCheck healthCheck = new InMemoryStoreHealthCheck(store);

        assertFalse(healthCheck.check().isHealthy());
    }

    private static class InMemoryStoreMock extends InMemoryStore {
        private final boolean running;

        InMemoryStoreMock(boolean running) {
            this.running = running;
        }

        @Override
        public boolean isRunning() {
            return running;
        }
    }
}
