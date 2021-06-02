package io.tackle.pathfinder.translation;

import io.tackle.pathfinder.dto.AssessmentCategoryDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.BasicDto;
import lombok.extern.java.Log;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

@Provider
@Log
@Produces(MediaType.APPLICATION_JSON)
public class TranslateProcessor implements MessageBodyWriter<BasicDto> {

    @Override
    public void writeTo(BasicDto t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
        WebApplicationException {
            log.info("writeTo : " + t + " type : " + type.getSimpleName() + " fields : \n" + Arrays.stream(type.getFields()).map(a-> a.getName() + " -- " + Arrays.stream(a.getAnnotations()).map(b -> b.annotationType().getSimpleName()).collect(Collectors.joining(","))).collect(Collectors.joining("\n")));

    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }
}