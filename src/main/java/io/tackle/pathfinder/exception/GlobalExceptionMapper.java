package io.tackle.pathfinder.exception;

import lombok.extern.java.Log;

import javax.transaction.RollbackException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Log
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {

        log.info("Capturing global exception : Cause : " + exception.getCause() + " Message : " + exception.getMessage());

        if (exception.getCause() instanceof RollbackException) {
            log.info( " Status 400 ");
            return Response.status(400).build();
        } else {
            log.info( " Status 500 ");
            return Response.status(500).build();
        }
    }
}
