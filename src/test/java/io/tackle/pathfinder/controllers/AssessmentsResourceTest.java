package io.tackle.pathfinder.controllers;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.pathfinder.dto.ApplicationDto;
import io.tackle.pathfinder.dto.AssessmentCategoryDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentQuestionDto;
import io.tackle.pathfinder.dto.AssessmentQuestionOptionDto;
import io.tackle.pathfinder.dto.AssessmentQuestionnaireDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.services.AssessmentSvc;
import lombok.extern.java.Log;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

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

	@Inject
    ManagedExecutor managedExecutor;

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
				.isEqualTo(AssessmentHeaderDto.builder().applicationId(20L).comment("").status(AssessmentStatus.STARTED).build());
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
	public void given_SameApplication_When_SeveralCreateAssessment_Then_Returns400() throws InterruptedException {
		CompletableFuture<ValidatableResponse> future1 = managedExecutor.supplyAsync(() -> {
			log.info("Async 1 request Assessment : " + LocalTime.now());
			ValidatableResponse response = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(new ApplicationDto(330L))
			.when()
				.post("/assessments")
			.then()
				.log().all()
				.statusCode(201);
			log.info("End Async 1 request Assessment : " + LocalTime.now());

			return response;
		});
		
		// To force second call starts a bit later than first one
		Thread.sleep(500);
		
		CompletableFuture<ValidatableResponse> future2 = managedExecutor.supplyAsync(() -> {
			log.info("Async 2 request Assessment : " + LocalTime.now());

			ValidatableResponse response =  given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(new ApplicationDto(330L))
			.when()
				.post("/assessments")
			.then()
				.log().all()
				.statusCode(400);

			log.info("End Async 2 request Assessment : " + LocalTime.now());
			return response;
		});

		assertThat(future1).succeedsWithin(Duration.ofSeconds(10));
		assertThat(future2).succeedsWithin(Duration.ofSeconds(10));
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
			.body("questionnaire.categories.find{it.order==2}.comment", is("This is a test comment"))
			.body("questionnaire.categories.find{it.order==5}.title", is("Application Cross-Cutting concerns"))
			.body("questionnaire.categories.find{it.order==5}.questions.size()", is(6))

			.body("questionnaire.categories.find{it.order==1}.questions.find{it.question=='What is the Mean Time to Recover (MTTR) when a fault is found with the application in production?'}.description", is("Gauge the problem resolution time, MTTR (mean time to recover) is the average time it takes to repair/recover a system"))
			.body("questionnaire.categories.find{it.order==1}.questions.find{it.question=='What is the Mean Time to Recover (MTTR) when a fault is found with the application in production?'}.options.size()", is(6))
			.body("questionnaire.categories.find{it.order==5}.questions.find{it.question=='How mature is the existing containerisation process, if any?'}.options.find{it.option=='Application containerisation not attempted as yet'}.checked", is(true));
	}

	@Test
	public void given_AssessmentCreated_When_UpdatingValues_Then_InformationIsStored_And_ResponseIsOK() {
		// Creation of the Assessment
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Retrieval of the assessment created
		AssessmentDto assessment = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(200)
			.extract().as(AssessmentDto.class);

		AssessmentCategoryDto category = assessment.getQuestionnaire().getCategories().get(0);
		AssessmentQuestionDto question = category.getQuestions().get(0);
		AssessmentQuestionOptionDto option = question.getOptions().get(0);

		// Modification of 1 category comment, 1 option selected, 2 stakeholders , 2 stakeholdergroups
		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		  .body(AssessmentDto.builder()
		  		.applicationId(500L)
				.id(header.getId())
				.questionnaire(
					AssessmentQuestionnaireDto.builder()
						.categories(List.of(
							AssessmentCategoryDto.builder()
							.id(category.getId())
							.comment("USER COMMENT 1")
							.questions(List.of(
								AssessmentQuestionDto.builder()
								.id(question.getId())
								.options(List.of(
									AssessmentQuestionOptionDto.builder()
									.id(option.getId())
									.checked(true)
									.build()
								))
								.build()
							))
							.build()))
						.build())
				.stakeholderGroups(List.of(1000L, 2000L))
				.stakeholders(List.of(444L, 555L))
		  		.build())
		.when()
			.patch("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(200)
			.body("id", equalTo(header.getId().intValue()),
				  "applicationId", equalTo(500),
				  "status", equalTo("STARTED"));

		// Retrieval of the assessment again to check updated values
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(200)
			.body("questionnaire.categories.find{it.id==" + category.getId() + "}.comment", is("USER COMMENT 1"))
			.body("questionnaire.categories.find{it.id==" + category.getId() + "}.questions.find{it.id==" + question.getId() + "}.options.find{it.id==" + option.getId() + "}.checked", is(true))
			.body("questionnaire.categories.find{it.id==" + category.getId() + "}.questions.find{it.id==" + question.getId() + "}.options.findAll{it.checked==true}.size()", is(1))
			.body("questionnaire.categories.find{it.id==" + category.getId() + "}.questions.find{it.id==" + question.getId() + "}.options.size()", greaterThan(1));
	}
	
	@Test
	public void given_AssessmentCreated_When_UpdatingStatus_Then_StatusIsStored_And_ResponseIsOK() {
		// Creation of the Assessment
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(5500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Modification of status to complete
		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		  .body(AssessmentDto.builder()
		  		.status(AssessmentStatus.COMPLETE)
		  		.build())
		.when()
			.patch("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(200)
			.body("id", equalTo(header.getId().intValue()),
				  "applicationId", equalTo(5500),
				  "status", equalTo("COMPLETE"));

		// Retrieval of the assessment again to check updated values
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("COMPLETE"));
	}	
	@Test
	public void given_AssessmentCreated_When_UpdatingStatusWithWrongValue_Then_ResponseIs400() {
		// Creation of the Assessment
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(6500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Modification of status to complete
		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		  .body("{ \"status\" : \"WHATEVER\"}")
		.when()
			.patch("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(400);

		// Retrieval of the assessment again to check updated values
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("STARTED"));
	}	
	
	@Test
	public void given_AssessmentCreated_When_UpdatingWithIncorrectIds_Then_ResponseIsBadRequest() {
		// Creation of the Assessment
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(7500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Retrieval of the assessment created
		AssessmentDto assessment = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(200)
			.extract().as(AssessmentDto.class);

		// Changing to an incorrect ID internally
		assessment.getQuestionnaire().getCategories().get(0).setId(assessment.getQuestionnaire().getCategories().get(0).getId() + 6000L);
		// Modification of status to complete
		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		  .body(assessment)
		.when()
			.patch("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(400);
	}	
	
	@Test
	public void given_AssessmentCreated_When_Deleting_Then_ResponseIsOKAndAssessmentIsNotReachable() {
		// Creation of the Assessment
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(29500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Retrieval of the assessment created
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(200);

		// Deleting the assessment
		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		.when()
			.delete("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(204);

		// Checking again the get
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(404);
	}	
	@Test
	public void given_AssessmentDeleted_When_Deleting_Then_ResponseIs404() {
		// Creation of the Assessment
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(19500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Retrieval of the assessment created
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(200);

		// Deleting the assessment
		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		.when()
			.delete("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(204);

		// Checking again the get
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.get("/assessments/" + header.getId())
		.then()
			.log().all()
			.statusCode(404);

		// Deleting the assessment again
		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		.when()
			.delete("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(404);
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

	@Test
	public void given_ApplicationAssessed_When_CopyAssessmentToAnotherNonAssessedApp_Then_NewAssessmentIsCreatedForTargetApplicationWithSameValues() {
		// Check that Application 59500 doesnt have any assessment
		Object[] elements = given()
			.queryParam("applicationId", "59500")
		.when()
			.get("/assessments")
		.then()
			.statusCode(200)
			.extract()
			.as(Object[].class);
		assertThat(elements).isEmpty();

		// Creation of the Assessment
		AssessmentHeaderDto assessmentSourceHeader = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(59500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201).extract().as(AssessmentHeaderDto.class);

		// Get the contents of the assessment
		AssessmentDto assessmentSource = given()
		.when()
			.get("/assessments/" + assessmentSourceHeader.getId())
		.then()
			.statusCode(200)
			.extract().as(AssessmentDto.class);

		// Check that Application 89500 doesnt have any assessment
		Object[] elementsTargetApp = given()
			.queryParam("applicationId", "89500")
		.when()
			.get("/assessments")
		.then()
			.statusCode(200)
			.extract()
			.as(Object[].class);
		assertThat(elementsTargetApp).isEmpty();

		// Copy of the Assessment
		AssessmentHeaderDto assessmentHeaderTarget = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(89500L))
		.when()
			.post("/assessments?fromAssessmentId=" + assessmentSourceHeader.getId())
		.then()
			.log().all()
			.statusCode(201)
			.body("status", is("STARTED"),
			      "applicationId", is(89500))
			.extract().as(AssessmentHeaderDto.class);

		// Check that Application 89500 now has an assessment
		AssessmentDto assessmentTarget = given()
		.when()
			.get("/assessments/" + assessmentHeaderTarget.getId())
		.then()
			.statusCode(200)
			.body("applicationId", is(89500))
			.extract().as(AssessmentDto.class);

		// Compare Values
		assertThat(assessmentTarget)
			.usingRecursiveComparison()
				.ignoringFieldsMatchingRegexes(".*\\.id",".*Id", ".*create.*", "update.*", "id")
				.ignoringCollectionOrder()
				.isEqualTo(assessmentSource);
	}

	public void given_ApplicationAssessed_When_CopyAssessmentToAnotherAssessedApp_Then_BadRequestIsAssessed() {
		// Creation of the Assessment
		AssessmentHeaderDto assessmentHeader = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(159500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Create another assessment
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(259500L))
		.when()
			.post("/assessments")
		.then()
			.statusCode(201);

		// Copy of the Assessment, and expect to fail
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(259500L))
		.when()
			.post("/assessments?fromAssessmentId=" + assessmentHeader.getId())
		.then()
			.log().all()
			.statusCode(400);
	}
	
	public void given_ApplicationAssessedButDeleted_When_CopyAssessmentToAnotherAssessedApp_Then_404NotFoundIsReturned() {
		// Creation of the Assessment
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(359500L))
		.when()
			.post("/assessments")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Deleting the assessment
		given()
		  .contentType(ContentType.JSON)
		  .accept(ContentType.JSON)
		.when()
			.delete("/assessments/" + header.getId())
		.then()
    		.log().all()
			.statusCode(204);

		// Copy of the Assessment, and expect to fail
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(389500L))
		.when()
			.post("/assessments/fromAssessmentId=" + header.getId())
		.then()
			.log().all()
			.statusCode(404);
	}

	public void given_ApplicationNotAssessed_When_CopyAssessmentToAnotherNotAssessedApp_Then_BadRequestIsAssessed() {
		//Copy of the Assessment, and expect to fail
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(new ApplicationDto(489500L))
		.when()
			.post("/assessments?fromAssessmentId=5556")
		.then()
			.log().all()
			.statusCode(404);
	}

	public void given_ApplicationAssessed_When_BulkCopyToListOfApplications_Then_CopyIsDoneAndResultIsListOfApplications() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/assessments/bulkcopy")
		.then()
	}
}
