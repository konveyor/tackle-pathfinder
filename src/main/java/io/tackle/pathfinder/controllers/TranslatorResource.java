/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
