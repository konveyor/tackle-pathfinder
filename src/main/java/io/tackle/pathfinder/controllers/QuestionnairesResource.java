package io.tackle.pathfinder.controllers;

import io.tackle.pathfinder.dto.questionnaire.QuestionnaireHeaderDto;
import io.tackle.pathfinder.services.QuestionnaireSvc;
import io.tackle.pathfinder.services.TranslatorSvc;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/questionnaires")
public class QuestionnairesResource {
    @Inject
    QuestionnaireSvc questionnaireSvc;

    @Inject
    TranslatorSvc translatorSvc;

    @Inject
    JsonWebToken accessToken;

    @GET
    @Produces("application/json")
    public List<QuestionnaireHeaderDto> getQuestionnaires(@QueryParam("language") String language) {
        String lang = translatorSvc.getLanguage(accessToken.getRawToken(), language);
        return questionnaireSvc.getQuestionnaireHeaderList(lang);
    }
}
