package io.tackle.pathfinder.controllers;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.tackle.pathfinder.AbstractResourceTest;
import io.tackle.pathfinder.DefaultTestProfile;
import io.tackle.pathfinder.NoAuthTestProfile;
import io.tackle.pathfinder.dto.ApplicationDto;
import io.tackle.pathfinder.dto.AssessmentBulkDto;
import io.tackle.pathfinder.dto.AssessmentBulkPostDto;
import io.tackle.pathfinder.dto.AssessmentCategoryDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentQuestionDto;
import io.tackle.pathfinder.dto.AssessmentQuestionOptionDto;
import io.tackle.pathfinder.dto.AssessmentQuestionnaireDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.dto.LandscapeDto;
import io.tackle.pathfinder.dto.RiskLineDto;
import io.tackle.pathfinder.dto.questionnaire.QuestionnaireHeaderDto;
import io.tackle.pathfinder.model.Risk;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.services.AssessmentSvc;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.Awaitility;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * None of the requests in this test should have authentication but still return the expected results
 */
@QuarkusTest
@TestProfile(NoAuthTestProfile.class)
@Log
public class NoAuthResourceTest {

    @Inject
    AssessmentSvc assessmentSvc;

    @BeforeEach
    @Transactional
    public void init() {
        Assessment.streamAll().forEach(PanacheEntityBase::delete);
    }

    @Test
    public void getAssessments() {
        given()
                .queryParam("applicationId", "20")
                .when()
                .get("/assessments")
                .then()
                .statusCode(200);
    }

    @Test
    public void getAssessmentById() {
        AssessmentHeaderDto assessment = assessmentSvc.newAssessment(null, 20L);

        given()
                .when()
                .get("/assessments/" + assessment.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void getQuestionnaires() {
        given()
                .when()
                .get("/questionnaires")
                .then()
                .statusCode(200);
    }

}
