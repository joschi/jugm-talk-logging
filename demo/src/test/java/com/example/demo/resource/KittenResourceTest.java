package com.example.demo.resource;

import com.example.demo.InMemoryStore;
import com.example.demo.model.ImmutableKitten;
import com.example.demo.model.Kitten;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class KittenResourceTest {
    private static final InMemoryStore store = new InMemoryStore();
    private static final Kitten kitten = ImmutableKitten.builder()
            .id(23)
            .name("Findus")
            .type(Kitten.KittenType.CUTE)
            .build();

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new KittenResource(store))
            .build();


    @BeforeClass
    public static void initialize() {
        store.put(kitten);
    }

    @Test
    public void testGetKitten() {
        final Kitten receivedKitten = resources.client()
                .target("/kittens/Findus")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Kitten.class);
        assertEquals(receivedKitten, kitten);
    }
}
