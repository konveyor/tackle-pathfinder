package io.tackle.pathfinder.exception;

import lombok.extern.java.Log;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Log
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        log.info("Capturing global exception : " + exception.getMessage());
        return Response.status(400).build();
    }
}
