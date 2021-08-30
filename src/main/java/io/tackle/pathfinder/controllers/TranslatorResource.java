package io.tackle.pathfinder.controllers;

import io.tackle.pathfinder.dto.translator.TranslateDBDto;
import io.tackle.pathfinder.services.TranslatorSvc;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/translator")
public class TranslatorResource {
    @Inject
    TranslatorSvc translatorSvc;

    @POST
    @Path("/translate")
    public Response translateText(@NotNull TranslateDBDto translateDBDto) {
        String key = translatorSvc.getKey(translateDBDto.getTable(), translateDBDto.getId(), translateDBDto.getField());
        translatorSvc.addOrUpdateTranslation(key, translateDBDto.getText(), translateDBDto.getLanguage());
        return Response
                .status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
