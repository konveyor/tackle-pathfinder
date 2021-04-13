package io.tackle.pathfinder.controllers;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.pathfinder.dto.ApplicationDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.services.AssessmentSvc;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
public class AssessmentsResourceTest extends SecuredResourceTest {
	@Inject
	AssessmentSvc assessmentSvc;

	@BeforeEach
	@Transactional
	public void init() {
		log.info("Assessments count : " + Assessment.count());
		log.info("Questionnaire count : " + Questionnaire.count());
		Assessment.streamAll().forEach(e -> e.delete());
		log.info("After delete Assessments count : " + Assessment.count());
		log.info("After delete Questionnaire count : " + Questionnaire.count());
	}

    @Test
	public void given_ApplicationWithAssessment_When_Get_Then_ReturnsHeaderDto() {
		assessmentSvc.createAssessment(20L);

		AssessmentHeaderDto[] assessments = given()
			.queryParam("applicationId", "20")
		.when()
			.get("/assessments")
		.then()
		   .statusCode(200)
				.extract().as(AssessmentHeaderDto[].class);

		assertThat(assessments)
			.hasSize(1)
			.usingRecursiveComparison()
				.ignoringFields("id")
				.isEqualTo(AssessmentHeaderDto.builder().applicationId(20L).notes("").status(AssessmentStatus.STARTED).build());
  	}

	@Test
	public void given_ApplicationWithoutAssessment_When_Get_Then_ReturnEmptyAnd200() {
		Object[] elements = given()
			.queryParam("applicationId", "30")
		.when()
			.get("/assessments")
		.then()
			.statusCode(200)
			.extract()
			.as(Object[].class);
		assertThat(elements).isEmpty();
  	}

	@Test
	public void given_ApplicationWithoutAssessment_When_CreateAssessment_Then_Returns201() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(20L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", greaterThan(0),
			      "applicationId", equalTo(20),
				  "status", equalTo("STARTED"));
	}

	@Test
	public void given_NullApplicationId_When_CreateAssessment_Then_Returns400() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto())
		.when()
			.post("/assessments")
		.then()
			.statusCode(400)	;
	}

	@Test
	public void given_ApplicationWithAssessment_When_CreateAssessment_Then_Returns400() {
		assessmentSvc.createAssessment(20L);

		given()
		    .contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(20L))
		.when()
			.post("/assessments")
		.then()
			.statusCode(400);
	}

	@Test
	public void given_NullApplicationId_When_GetAssessments_Then_Returns400() {
		given()
		    .contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments")
		.then()
			.statusCode(400);
	}

	@Test
	public void given_NullApplication_When_CreateAssessment_Then_Returns400() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(400);
	}

	@Test

	public void given_Assessment_When_GetAssessment_Then_ReturnsAssessmentQuestionnaire() throws InterruptedException {
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(400L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		addUserEnteredInfoToAssessment(header.getId());

		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(200)
			.body("applicationId", is(400))
			.body("status", is("STARTED"))
			.body("stakeholders.size()", is(3))
			.body("stakeholderGroups.size()", is(2))
			.body("questionnaire.categories.size()", is(5))
			.body("questionnaire.categories.findAll{it.order==2}[0].comment", is("This is a test comment"))
			.body("questionnaire.categories.findAll{it.order==5}[0].title", is("Application Cross-Cutting concerns"))
			.body("questionnaire.categories.findAll{it.order==5}[0].questions.size()", is(6))

			.body("questionnaire.categories.findAll{it.order==1}[0].questions.findAll{it.question=='What is the Mean Time to Recover (MTTR) when a fault is found with the application in production?'}[0].description", is("Gauge the problem resolution time, MTTR (mean time to recover) is the average time it takes to repair/recover a system"))
			.body("questionnaire.categories.findAll{it.order==1}[0].questions.findAll{it.question=='What is the Mean Time to Recover (MTTR) when a fault is found with the application in production?'}[0].options.size()", is(6))
			.body("questionnaire.categories.findAll{it.order==5}[0].questions.findAll{it.question=='How mature is the existing containerisation process, if any?'}[0].options.findAll{it.option=='Application containerisation not attempted as yet'}[0].checked", is(true));
	}

	@Transactional
	public void addUserEnteredInfoToAssessment(Long assessmentId) {
		Assessment assessment = Assessment.findById(assessmentId);
		AssessmentStakeholder stakeholder = AssessmentStakeholder.builder().assessment(assessment).stakeholderId(1100L)
				.build();
		stakeholder.persist();
		assessment.stakeholders.add(stakeholder);

		stakeholder = AssessmentStakeholder.builder().assessment(assessment).stakeholderId(1200L).build();
		stakeholder.persist();
		assessment.stakeholders.add(stakeholder);

		stakeholder = AssessmentStakeholder.builder().assessment(assessment).stakeholderId(1300L).build();
		stakeholder.persist();
		assessment.stakeholders.add(stakeholder);

		AssessmentStakeholdergroup group = AssessmentStakeholdergroup.builder().assessment(assessment).stakeholdergroupId(1500L).build();
		group.persist();
		assessment.stakeholdergroups.add(group);

		group = AssessmentStakeholdergroup.builder().assessment(assessment).stakeholdergroupId(1600L).build();
		group.persist();
		assessment.stakeholdergroups.add(group);

		AssessmentSingleOption.update("set selected = true where option = 'Application containerisation not attempted as yet'");
		AssessmentCategory.update("set comment = 'This is a test comment' where name='Application Dependencies'");
    }

}
