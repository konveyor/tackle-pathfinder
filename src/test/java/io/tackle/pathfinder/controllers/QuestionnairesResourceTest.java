package io.tackle.pathfinder.controllers;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.dto.questionnaire.*;
import io.tackle.pathfinder.model.Risk;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.services.QuestionnaireSvc;
import io.tackle.pathfinder.services.TranslatorSvc;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(value = PostgreSQLDatabaseTestResource.class,
    initArgs = {
        @ResourceArg(name = PostgreSQLDatabaseTestResource.DB_NAME, value = "pathfinder_db"),
        @ResourceArg(name = PostgreSQLDatabaseTestResource.USER, value = "pathfinder"),
        @ResourceArg(name = PostgreSQLDatabaseTestResource.PASSWORD, value = "pathfinder")
    }
)
@QuarkusTestResource(value = KeycloakTestResource.class,
    initArgs = {
        @ResourceArg(name = KeycloakTestResource.IMPORT_REALM_JSON_PATH, value = "keycloak/quarkus-realm.json"),
        @ResourceArg(name = KeycloakTestResource.REALM_NAME, value = "quarkus")
    }
)
@Log
public class QuestionnairesResourceTest extends SecuredResourceTest {
    @Inject
    TranslatorSvc translatorSvc;

    @Inject
    UserTransaction userTransaction;

    @Test
    public void given_Questionnaire_When_GetList_Then_ReturnsHeaderDto() {
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
    public void given_Questionnaire_When_GetListInADifferentLanguage_Then_ReturnsHeaderDtoInThatLanguage() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
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

    @Test
    public void given_Questionnaire_When_Get_Then_ReturnsContentDto() {
        Questionnaire questionnaire = Questionnaire.findAll().firstResult();

        QuestionnaireDto questionnaireDto = given()
            .pathParam("questionnaireId", questionnaire.id)
            .when()
            .get("/questionnaires/{questionnaireId}")
            .then()
            .statusCode(200)
            .extract().as(QuestionnaireDto.class);

        assertThat(questionnaireDto.getLanguage()).isEqualTo("EN");
        assertThat(questionnaireDto.getCategories()).hasSize(5);

        CategoryDto categoryDto = questionnaireDto.getCategories().stream().filter(a -> a.getOrder() == 3).findFirst().get();
        assertThat(categoryDto).extracting(a -> a.getTitle()).isEqualTo("Application architecture");

        QuestionDto questionDto = categoryDto.getQuestions().stream().filter(a -> a.getOrder() == 4).findFirst().get();
        assertThat(questionDto).extracting(a -> a.getQuestion()).isEqualTo("How does the application handle service discovery?");
        assertThat(questionDto).extracting(a -> a.getDescription()).isEqualTo("How does the application discover services?");

        QuestionOptionDto questionOptionDto = questionDto.getOptions().stream().filter(a -> a.getOrder() == 2).findFirst().get();
        assertThat(questionOptionDto).extracting(a -> a.getOption()).isEqualTo("Requires an application or cluster restart to discover new service instances");
        assertThat(questionOptionDto).extracting(a -> a.getRisk()).isEqualTo(Risk.RED);
    }
}