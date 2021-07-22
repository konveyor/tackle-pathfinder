package io.tackle.pathfinder.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {
    @Override
    public Response toResponse(InvalidFormatException exception) {
        return Response.status(400).build();
    }
}
