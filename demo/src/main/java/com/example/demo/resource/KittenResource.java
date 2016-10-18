package com.example.demo.resource;

import com.codahale.metrics.annotation.Timed;
import com.example.demo.InMemoryStore;
import com.example.demo.model.Kitten;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@Path("/kittens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("Nice kittens")
public class KittenResource {
    private final InMemoryStore store;

    public KittenResource(final InMemoryStore store) {
        this.store = store;
    }

    @GET
    @Timed
    @ApiOperation("Show all kittens")
    public Collection<Kitten> getAllKittens() {
        return store.getAll();
    }

    @POST
    @Timed
    @ApiOperation("Add cute kitten")
    public Response addKitten(@Valid final Kitten kitten,
                              @Context final UriInfo uriInfo) throws URISyntaxException {
        store.put(kitten);

        return Response.created(new URI("/" + kitten.name())).build();
    }

    @GET
    @Path("{name}")
    @ApiOperation("Get kitten")
    public Optional<Kitten> getKitten(@ApiParam("Name of the Kitten to retrieve")
                                      @PathParam("name") String name) {
        return store.get(name);
    }

    @DELETE
    @Path("{name}")
    @ApiOperation("Delete kitten")
    @RolesAllowed("admin")
    public void deleteKitten(@ApiParam("Name of the Kitten to delete")
                             @PathParam("name") String name) {
        if (!store.delete(name)) {
            throw new NotFoundException("Kitten \"" + name + "\" not found.");
        }
    }

    @GET
    @Path("/fail")
    @ApiOperation("Fail spectacularly")
    public Kitten failKitten() {
        return store.fail();
    }
}
