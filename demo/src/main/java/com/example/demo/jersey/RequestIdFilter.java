package com.example.demo.jersey;

import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.util.UUID;

@Priority(0)
public class RequestIdFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final String REQUEST_ID_HEADER = "Request-ID";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final String requestIdHeader = requestContext.getHeaderString(REQUEST_ID_HEADER);
        final String requestID = requestIdHeader == null ? UUID.randomUUID().toString() : requestIdHeader;
        requestContext.setProperty("request_id", requestID);
        MDC.put("request_id", requestID);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final String requestId = (String) requestContext.getProperty("request_id");
        responseContext.getHeaders().putSingle(REQUEST_ID_HEADER, requestId);
        MDC.remove("request_id");
    }
}
