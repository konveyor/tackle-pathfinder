package io.tackle.pathfinder.controllers;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.pathfinder.dto.questionnaire.CategoryDto;
import io.tackle.pathfinder.dto.questionnaire.QuestionDto;
import io.tackle.pathfinder.dto.questionnaire.QuestionOptionDto;
import io.tackle.pathfinder.dto.questionnaire.QuestionnaireDto;
import io.tackle.pathfinder.dto.translator.TranslateDBDto;
import io.tackle.pathfinder.model.Risk;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

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
class TranslatorResourceTest extends SecuredResourceTest {
    @Test
    public void given_Translation_When_Posted_Then_ReturnsContentDto() {
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

        TranslateDBDto translateDto = TranslateDBDto.builder()
            .id(categoryDto.getId())
            .table("Category")
            .field("name")
            .text("FR: Application Architecture")
            .language("FR")
            .build();

        given()
            .contentType(ContentType.JSON)
            .body(translateDto)
            .when()
            .post("/translator/translate")
            .then()
            .statusCode(201);

        QuestionnaireDto questionnaireTranslatedDto = given()
            .pathParam("questionnaireId", questionnaire.id)
            .queryParam("language", "FR")
            .when()
            .get("/questionnaires/{questionnaireId}")
            .then()
            .statusCode(200)
            .extract().as(QuestionnaireDto.class);

        assertThat(questionnaireTranslatedDto.getLanguage()).isEqualTo("FR");
        assertThat(questionnaireTranslatedDto.getCategories()).hasSize(5);

        CategoryDto categoryTranslatedDto = questionnaireTranslatedDto.getCategories().stream().filter(a -> a.getOrder() == 3).findFirst().get();
        assertThat(categoryTranslatedDto).extracting(a -> a.getTitle()).isEqualTo("FR: Application Architecture");
    }
}