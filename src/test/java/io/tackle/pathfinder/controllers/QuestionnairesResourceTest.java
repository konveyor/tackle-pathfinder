package io.tackle.pathfinder.controllers;

import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.dto.questionnaire.QuestionnaireHeaderDto;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.services.QuestionnaireSvc;
import io.tackle.pathfinder.services.TranslatorSvc;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class QuestionnairesResourceTest extends SecuredResourceTest {
    @Inject
    TranslatorSvc translatorSvc;

    @Inject
    UserTransaction userTransaction;

    @Test
    public void given_Questionnaire_When_Get_Then_ReturnsHeaderDto() {
        QuestionnaireHeaderDto[] questionnaires = given()
            .queryParam("language", "EN")
            .when()
            .get("/questionnaires")
            .then()
            .statusCode(200)
            .extract().as(QuestionnaireHeaderDto[].class);

        assertThat(questionnaires)
            .hasSizeGreaterThan(0)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(QuestionnaireHeaderDto.builder()
                .name("Pathfinder")
                .language("EN")
                .build());
    }

    @Test
    public void given_Questionnaire_When_GetInADifferentLanguage_Then_ReturnsHeaderDtoInThatLanguage() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        userTransaction.begin();
        translatorSvc.addOrUpdateTranslation(Questionnaire.findAll().firstResult(), "name", "CA Pathfinder", "CA");
        userTransaction.commit();

        QuestionnaireHeaderDto[] questionnaires = given()
            .queryParam("language", "CA")
            .when()
            .get("/questionnaires")
            .then()
            .statusCode(200)
            .extract().as(QuestionnaireHeaderDto[].class);

        assertThat(questionnaires)
            .hasSizeGreaterThan(0)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(QuestionnaireHeaderDto.builder()
                .name("CA Pathfinder")
                .language("CA")
                .build());
    }
}