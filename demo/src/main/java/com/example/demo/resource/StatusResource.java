package com.example.demo.resource;

import com.codahale.metrics.annotation.Counted;
import com.example.demo.view.StatusView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;

@Path("/status")
@Produces(MediaType.TEXT_HTML)
@Api("HTTP Cats")
public class StatusResource {
    private static final Logger log = LoggerFactory.getLogger(StatusResource.class);
    private final int[] statusCodes = {100, 101, 200, 201, 202, 206, 207,
            400, 403, 404, 405, 406, 408, 409, 410, 411, 412, 413, 414,
            415, 416, 417, 418, 422, 423, 424, 425, 426, 431, 444, 450, 451,
            500, 502, 502, 506, 507, 508, 509, 599};

    private final Random random = new Random();

    @GET
    @ApiOperation("Create an HTTP response with given status code")
    @Counted
    public Response getResponse(@QueryParam("code")
                                @ApiParam("HTTP response code")
                                @Range(min = 100, max = 599) Integer code) {
        final int statusCode;
        if (code == null) {
            statusCode = statusCodes[random.nextInt(statusCodes.length)];
        } else {
            statusCode = code;
        }

        log.info("Returning HTTP response with status code {}", statusCode);
        return Response.status(statusCode)
                .entity(new StatusView(statusCode))
                .build();
    }
}
