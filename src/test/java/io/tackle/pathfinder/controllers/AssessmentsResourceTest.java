package io.tackle.pathfinder.controllers;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.commons.tests.SecuredResourceTest;
import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.model.Risk;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.services.AssessmentSvc;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.Awaitility;


import javax.inject.Inject;
import javax.transaction.*;

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

	@Inject
	UserTransaction userTransaction;

	@BeforeEach
	@Transactional
	public void init() {
		log.info("Assessments count : " + Assessment.count());
		log.info("Questionnaire count : " + Questionnaire.count());
		Assessment.streamAll().forEach(PanacheEntityBase::delete);
		log.info("After delete Assessments count : " + Assessment.count());
		log.info("After delete Questionnaire count : " + Questionnaire.count());
	}

    @Test
	public void given_ApplicationWithAssessment_When_Get_Then_ReturnsHeaderDto() {
		assessmentSvc.newAssessment(null, 20L);

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
		assessmentSvc.newAssessment(null, 20L);

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
				.log().all();
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
				.log().all();

			log.info("End Async 2 request Assessment : " + LocalTime.now());
			return response;
		});

		Awaitility.await()
			.atMost(Duration.ofSeconds(10))
			.untilAsserted(() -> {
				assertThat(future1.isDone()).isTrue();
				assertThat(future2.isDone()).isTrue();
				assertThat((future1.get().extract().statusCode() == 201 &&
							future2.get().extract().statusCode() == 400) ||
						(future1.get().extract().statusCode() == 400 &&
							future2.get().extract().statusCode() == 201)).isTrue();
			});

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
			.body("questionnaire.categories.find{it.order==5}.title", is("Application cross-cutting concerns"))
			.body("questionnaire.categories.find{it.order==5}.questions.size()", is(6))

			.body("questionnaire.categories.find{it.order==1}.questions.find{it.question=='What is the application\\\'s mean time to recover (MTTR) from failure in a production environment?'}.description", is("Average time for the application to recover from failure"))
			.body("questionnaire.categories.find{it.order==1}.questions.find{it.question=='What is the application\\\'s mean time to recover (MTTR) from failure in a production environment?'}.options.size()", is(6))
			.body("questionnaire.categories.find{it.order==5}.questions.find{it.question=='How mature is the containerization process, if any?'}.options.find{it.option=='Application containerization has not yet been attempted'}.checked", is(true))
  		    .body("questionnaire.categories.find{it.order==5}.questions.find{it.question=='How mature is the containerization process, if any?'}.options.find{it.option=='Application containerization has not yet been attempted'}.risk", is("GREEN"));

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

		AssessmentSingleOption.update("set selected = true where option = 'Application containerization has not yet been attempted'");
		AssessmentCategory.update("set comment = 'This is a test comment' where name='Application dependencies'");
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

	@Test
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

	@Test
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
			.post("/assessments?fromAssessmentId=" + header.getId())
		.then()
			.log().all()
			.statusCode(404);
	}

	@Test
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

	@Test
	public void given_ApplicationAssessed_When_BulkCreateListOfApplications_Then_CreationIsDoneAndResultIsListOfApplications() {
		AssessmentBulkDto headerBulk = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(AssessmentBulkPostDto.builder()
					.applications(List.of(ApplicationDto.builder().applicationId(999L).build(),
							ApplicationDto.builder().applicationId(888L).build(),
							ApplicationDto.builder().applicationId(777L).build()))
					.build())
		.when()
			.post("/assessments/bulk")
		.then()
			.log().all()
			.statusCode(202)
			.extract().as(AssessmentBulkDto.class);

		Awaitility.await()
		.atMost(50, TimeUnit.SECONDS)
		.pollInterval(Duration.ofSeconds(5))
		.untilAsserted(() -> {
			log.info("Calling");
			AssessmentBulkDto bulkDtos = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.get("/assessments/bulk/" + headerBulk.getBulkId())
			.then()
				.log().all()
				.statusCode(200).extract().as(AssessmentBulkDto.class);

			assertThat(bulkDtos.getAssessments()).hasSize(3);
			assertThat(bulkDtos.getAssessments()).allMatch(e -> StringUtils.isBlank(e.getError()));
			assertThat(bulkDtos.getAssessments()).allMatch(e -> null != e.getId());
			assertThat(bulkDtos.getCompleted()).isTrue();
		});
	}

	@Test
	public void given_ApplicationAssessed_When_BulkCopyListOfApplications_Then_CopyIsDoneAndResultIsListOfApplications() throws InterruptedException {
		// Creation of the Assessment
		AssessmentHeaderDto assessmentHeaderDto = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(new ApplicationDto(99999L))
				.when()
				.post("/assessments")
				.then()
				.statusCode(201)
				.extract().as(AssessmentHeaderDto.class);

		// Retrieval of the assessment created
		AssessmentDto assessmentSource = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.get("/assessments/" + assessmentHeaderDto.getId())
			.then()
			.log().all()
			.statusCode(200)
			.extract().as(AssessmentDto.class);

		AssessmentCategoryDto category = assessmentSource.getQuestionnaire().getCategories().get(0);
		AssessmentQuestionDto question = category.getQuestions().get(0);
		AssessmentQuestionOptionDto option = question.getOptions().get(0);

		// Modification of 1 category comment, 1 option selected, 2 stakeholders , 2 stakeholdergroups
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(AssessmentDto.builder()
				.applicationId(99999L)
				.id(assessmentHeaderDto.getId())
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
			.patch("/assessments/" + assessmentHeaderDto.getId())
			.then()
			.log().all()
			.statusCode(200)
			.body("id", equalTo(assessmentHeaderDto.getId().intValue()),
				"applicationId", equalTo(99999),
				"status", equalTo("STARTED"));

		AssessmentBulkDto headerBulk = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(AssessmentBulkPostDto.builder()
						.fromAssessmentId(assessmentHeaderDto.getId())
						.applications(List.of(ApplicationDto.builder().applicationId(1999L).build(),
								ApplicationDto.builder().applicationId(1888L).build(),
								ApplicationDto.builder().applicationId(1666L).build(),
								ApplicationDto.builder().applicationId(1777L).build()
								))
						.build())
		.when()
			.post("/assessments/bulk")
		.then()
			.log().all()
			.statusCode(202)
			.extract().as(AssessmentBulkDto.class);

		Awaitility.await()
		.atMost(50, TimeUnit.SECONDS)
		.untilAsserted(() -> {
			AssessmentBulkDto bulkDto = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.get("/assessments/bulk/" + headerBulk.getBulkId())
			.then()
				.log().all()
				.statusCode(200).extract().as(AssessmentBulkDto.class);

			assertThat(bulkDto.getAssessments()).hasSize(4);
			assertThat(bulkDto.getAssessments()).allMatch(e -> StringUtils.isBlank(e.getError()));
			assertThat(bulkDto.getAssessments()).allMatch(e -> null != e.getId());
			assertThat(bulkDto.getCompleted()).isTrue();
		});

		AssessmentHeaderDto[] assessments = given()
			.queryParam("applicationId", "1888")
			.when()
			.get("/assessments")
			.then()
			.statusCode(200)
			.extract().as(AssessmentHeaderDto[].class);

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.get("/assessments/" + assessments[0].getId())
			.then()
			.log().all()
			.statusCode(200)
			.body("applicationId", is(1888))
			.body("status", is("STARTED"))
			.body("stakeholders.size()", is(2))
			.body("stakeholderGroups.size()", is(2))
			.body("questionnaire.categories.size()", is(5))
			.body("questionnaire.categories.find{it.order==" + category.getOrder() + "}.comment", is("USER COMMENT 1"))
			.body("questionnaire.categories.find{it.order==" + category.getOrder() + "}.title", is(category.getTitle()))
			.body("questionnaire.categories.find{it.order==" + category.getOrder() + "}.questions.size()", is(category.getQuestions().size()))

			.body("questionnaire.categories.find{it.order==" + category.getOrder() + "}.questions.find{it.order==" + question.getOrder() + "}.description", is(question.getDescription()))
			.body("questionnaire.categories.find{it.order==" + category.getOrder() + "}.questions.find{it.order==" + question.getOrder() + "}.options.size()", is(question.getOptions().size() ))
			.body("questionnaire.categories.find{it.order==" + category.getOrder() + "}.questions.find{it.order==" + question.getOrder() + "}.options.find{it.order==" + option.getOrder() + "}.checked", is(true));

	}

	@Test
	public void bulkCopyShouldOverrideAssessments() throws InterruptedException {
		// Case: create an assessment for app1 and app2, then copy assessment from app1 to app3;
		// Finally copy (override) assessment from app2 to app3

		Long application1Id = 1L;
		Long application2Id = 2L;
		Long application3Id = 3L;

		// Assessment for app1
		AssessmentHeaderDto assessmentApp3 = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(new ApplicationDto(application1Id))
				.when()
				.post("/assessments")
				.then()
				.statusCode(201)
				.extract().as(AssessmentHeaderDto.class);

		given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(AssessmentDto.builder()
						.stakeholders(List.of(11L, 111L))
						.status(AssessmentStatus.COMPLETE)
						.build()
				)
				.when()
				.patch("/assessments/" + assessmentApp3.getId())
				.then()
				.statusCode(200)
				.body("id", equalTo(assessmentApp3.getId().intValue()),
						"applicationId", equalTo(application1Id.intValue()),
						"status", equalTo(AssessmentStatus.COMPLETE.toString())
				);

		// Assessment for app2
		AssessmentHeaderDto assessmentApp2 = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(new ApplicationDto(application2Id))
				.when()
				.post("/assessments")
				.then()
				.statusCode(201)
				.extract().as(AssessmentHeaderDto.class);

		given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(AssessmentDto.builder()
						.stakeholders(List.of(22L, 222L))
						.status(AssessmentStatus.STARTED)
						.build()
				)
				.when()
				.patch("/assessments/" + assessmentApp2.getId())
				.then()
				.statusCode(200)
				.body("id", equalTo(assessmentApp2.getId().intValue()),
						"applicationId", equalTo(application2Id.intValue()),
						"status", equalTo(AssessmentStatus.STARTED.toString())
				);

		// Create bulk copy: source=app1, target=app3
		AssessmentBulkDto headerBulk1 = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(AssessmentBulkPostDto.builder()
						.fromAssessmentId(assessmentApp3.getId())
						.applications(List.of(
								ApplicationDto.builder()
										.applicationId(application3Id)
										.build()
						))
						.build()
				)
				.when()
				.post("/assessments/bulk")
				.then()
				.statusCode(202)
				.extract().as(AssessmentBulkDto.class);

		Awaitility.await()
				.atMost(50, TimeUnit.SECONDS)
				.untilAsserted(() -> {
					AssessmentBulkDto bulkDto = given()
							.contentType(ContentType.JSON)
							.accept(ContentType.JSON)
							.when()
							.get("/assessments/bulk/" + headerBulk1.getBulkId())
							.then()
							.statusCode(200).extract().as(AssessmentBulkDto.class);

					assertThat(bulkDto.getCompleted()).isTrue();
				});

		assessmentApp3 = given()
				.queryParam("applicationId", application3Id)
				.when()
				.get("/assessments")
				.then()
				.statusCode(200)
				.extract().as(AssessmentHeaderDto[].class)[0];

		given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get("/assessments/" + assessmentApp3.getId())
				.then()
				.statusCode(200)
				.body("applicationId", is(application3Id.intValue()),
						"status", is(AssessmentStatus.COMPLETE.toString()),
						"stakeholders.size()", is(2),
						"stakeholders[0]", is(11),
						"stakeholders[1]", is(111)
				);

		// Create bulk copy: source=app2, target=app3
		AssessmentBulkDto headerBulk2 = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(AssessmentBulkPostDto.builder()
						.fromAssessmentId(assessmentApp2.getId())
						.applications(List.of(
								ApplicationDto.builder()
										.applicationId(application3Id)
										.build()
						))
						.build()
				)
				.when()
				.post("/assessments/bulk")
				.then()
				.statusCode(202)
				.extract().as(AssessmentBulkDto.class);

		Awaitility.await()
				.atMost(50, TimeUnit.SECONDS)
				.untilAsserted(() -> {
					AssessmentBulkDto bulkDto = given()
							.contentType(ContentType.JSON)
							.accept(ContentType.JSON)
							.when()
							.get("/assessments/bulk/" + headerBulk2.getBulkId())
							.then()
							.statusCode(200).extract().as(AssessmentBulkDto.class);

					assertThat(bulkDto.getCompleted()).isTrue();
				});

		assessmentApp3 = given()
				.queryParam("applicationId", application3Id)
				.when()
				.get("/assessments")
				.then()
				.statusCode(200)
				.extract().as(AssessmentHeaderDto[].class)[0];

		given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get("/assessments/" + assessmentApp3.getId())
				.then()
				.statusCode(200)
				.body("applicationId", is(application3Id.intValue()),
						"status", is(AssessmentStatus.STARTED.toString()),
						"stakeholders.size()", is(2),
						"stakeholders[0]", is(22),
						"stakeholders[1]", is(222)
				);
	}

	@Test
	public void given_ApplicationsAssessed_When_LandscapeRequested_Then_ExpectedJSONIsReturned() {
		// create 2 assessments
		// Creation of the Assessment
		AssessmentHeaderDto header1 = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(new ApplicationDto(659500L))
			.when()
				.post("/assessments")
			.then()
				.statusCode(201)
				.extract().as(AssessmentHeaderDto.class);
		// Creation of the Assessment
		AssessmentHeaderDto header2 = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(new ApplicationDto(669500L))
			.when()
				.post("/assessments")
			.then()
				.statusCode(201)
				.extract().as(AssessmentHeaderDto.class);

		// get both assessments
		// Get the contents of the assessment
		AssessmentDto assessmentSource1 = given()
			.when()
			.get("/assessments/" + header1.getId())
			.then()
			.statusCode(200)
			.extract().as(AssessmentDto.class);
		// Get the contents of the assessment
		AssessmentDto assessmentSource2 = given()
			.when()
			.get("/assessments/" + header2.getId())
			.then()
			.statusCode(200)
			.extract().as(AssessmentDto.class);

		// answer questions and complete the assessment
		assessmentSource1.getQuestionnaire().getCategories().forEach(a -> a.getQuestions().forEach(b -> b.getOptions().stream().filter(c -> c.getRisk() == Risk.GREEN).findFirst().ifPresent(d -> d.setChecked(true))));
		assessmentSource2.getQuestionnaire().getCategories().forEach(a -> a.getQuestions().forEach(b -> b.getOptions().stream().filter(c -> c.getRisk() == Risk.RED).findFirst().ifPresent(d -> d.setChecked(true))));
		assessmentSource1.setStatus(AssessmentStatus.COMPLETE);
		assessmentSource2.setStatus(AssessmentStatus.COMPLETE);

		// Update assessments
		// Modification of status to complete
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(assessmentSource1)
			.when()
			.patch("/assessments/" + header1.getId())
			.then()
			.statusCode(200)
			.body("id", equalTo(header1.getId().intValue()),
				"status", equalTo("COMPLETE"));

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(assessmentSource2)
			.when()
			.patch("/assessments/" + header2.getId())
			.then()
			.statusCode(200)
			.body("id", equalTo(header2.getId().intValue()),
				"status", equalTo("COMPLETE"));

		// request Landscape
		LandscapeDto[] landscape = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(List.of(new ApplicationDto(659500L), new ApplicationDto(669500L)))
			.when()
				.post("/assessments/assessment-risk")
			.then()
				.statusCode(200)
			.extract().as(LandscapeDto[].class);

		// assert
		assertThat(landscape).containsExactlyInAnyOrder(new LandscapeDto(header1.getId(), Risk.GREEN, header1.getApplicationId()), new LandscapeDto(header2.getId(), Risk.RED, header2.getApplicationId()));
	}

	@Test
	public void given_ApplicationList_WhenIdentifiedRisks_Then_ResultListOfAnswers() {
		// Creation of the Assessment
		AssessmentHeaderDto header = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(new ApplicationDto(15500L))
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
				.statusCode(200)
				.extract().as(AssessmentDto.class);

		AssessmentCategoryDto category = assessment.getQuestionnaire().getCategories().get(0);
		AssessmentQuestionDto question = category.getQuestions().get(0);
		AssessmentQuestionOptionDto optionRed = question.getOptions().stream().filter(a -> a.getRisk() == Risk.RED).findFirst().get();
		AssessmentQuestionOptionDto optionamber = question.getOptions().stream().filter(a -> a.getRisk() == Risk.AMBER).findFirst().get();

		// Modification of 1 category comment, 1 option selected, 2 stakeholders , 2 stakeholdergroups
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(AssessmentDto.builder()
				.applicationId(15500L)
				.id(header.getId())
				.questionnaire(AssessmentQuestionnaireDto.builder()
					.categories(List.of(AssessmentCategoryDto.builder()
						.id(category.getId())
						.comment("USER COMMENT 1")
						.questions(List.of(
							AssessmentQuestionDto.builder()
								.id(question.getId())
								.options(List.of(
									AssessmentQuestionOptionDto.builder()
										.id(optionRed.getId())
										.checked(true)
										.build(),
									AssessmentQuestionOptionDto.builder()
										.id(optionamber.getId())
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
				.statusCode(200)
				.body("id", equalTo(header.getId().intValue()),
						"applicationId", equalTo(15500),
						"status", equalTo("STARTED"));

		RiskLineDto[] riskLineDtos = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(List.of(new ApplicationDto(15500L),new ApplicationDto(589500L),new ApplicationDto(689500L)))
		.when()
			.post("/assessments/risks")
		.then()
			.log().all()
			.statusCode(200)
		.extract().as(RiskLineDto[].class);

		assertThat(riskLineDtos).hasSize(1);
		assertThat(riskLineDtos[0].getApplications()).containsExactlyInAnyOrder(15500L);
	}

	@Test
	public void given_EmptyListOfApplications_when_IdentifiedRisks_then_HTTP400() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post("/assessments/risks")
		.then()
			.log().all()
			.statusCode(400);
	}

	@Test
	public void given_ListOfApplicationsWithNoAssessments_when_IdentifiedRisks_then_EmptyList() {
		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(List.of(new ApplicationDto(91L), new ApplicationDto(92L), new ApplicationDto(93L)))
		.when()
			.post("/assessments/risks")
		.then()
			.statusCode(200)
			.body("size()", is(0));
	}

	@Test
	public void given_ApplicationsAssessed_When_Confidence_Then_ResultIsTheExpected() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
		userTransaction.begin();
		// create assessment
		AssessmentHeaderDto assessmentREDHeader = assessmentSvc.createAssessment( 20008L);
		AssessmentHeaderDto assessmentGREENHeader = assessmentSvc.createAssessment( 20009L);
		AssessmentHeaderDto assessmentAMBERHeader = assessmentSvc.createAssessment( 20010L);
		AssessmentHeaderDto assessmentUNKNOWNHeader = assessmentSvc.createAssessment( 20011L);
		Assessment assessmentRED =  Assessment.findById(assessmentREDHeader.getId());
		Assessment assessmentGREEN =  Assessment.findById(assessmentGREENHeader.getId());
		Assessment assessmentAMBER =  Assessment.findById(assessmentAMBERHeader.getId());
		Assessment assessmentUNKNOWN =  Assessment.findById(assessmentUNKNOWNHeader.getId());

		// answer questions
		assessmentRED.status = AssessmentStatus.COMPLETE;
		assessmentRED.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.RED).findFirst().ifPresent(b -> b.selected = true)));
		assessmentGREEN.status = AssessmentStatus.COMPLETE;
		assessmentGREEN.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.GREEN).findFirst().ifPresent(b -> b.selected = true)));
		assessmentAMBER.status = AssessmentStatus.COMPLETE;
		assessmentAMBER.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.AMBER).findFirst().ifPresent(b -> b.selected = true)));
		assessmentUNKNOWN.status = AssessmentStatus.COMPLETE;
		assessmentUNKNOWN.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.UNKNOWN).findFirst().ifPresent(b -> b.selected = true)));

		userTransaction.commit();

		given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(List.of(new ApplicationDto(20008L),new ApplicationDto(20009L),new ApplicationDto(20010L),new ApplicationDto(20011L)))
		.when()
			.post("/assessments/confidence")
		.then()
			.log().all()
			.statusCode(200)
			.body("find{it.assessmentId=="+ assessmentRED.id + "}.confidence", is(0))
			.body("find{it.assessmentId=="+ assessmentGREEN.id + "}.confidence", is(100))
			.body("find{it.assessmentId=="+ assessmentAMBER.id + "}.confidence", is(24))
			.body("find{it.assessmentId=="+ assessmentUNKNOWN.id + "}.confidence", is(70));

	}

	@Test
	public void given_AssessmentAndTranslations_when_TranslationDeleted_then_ThatConceptHasTheNotTranslatedVallue() {
		String KEYCLOAK_SERVER_URL = ConfigProvider.getConfig().getOptionalValue("quarkus.oidc.auth-server-url", String.class).orElse("http://localhost:8180/auth");
		String ACCESS_TOKEN_JDOE = RestAssured.given().relaxedHTTPSValidation()
			.auth().preemptive()
				.basic("backend-service", "secret")
				.contentType("application/x-www-form-urlencoded")
				.formParam("grant_type", "password")
				.formParam("username", "jdoe")
				.formParam("password", "jdoe")
			.when()
				.post(KEYCLOAK_SERVER_URL + "/protocol/openid-connect/token", new Object[0])
			.then()
				.extract()
				.path("access_token", new String[0]).toString();

		// Creation of the Assessment
		AssessmentHeaderDto header = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON).auth().oauth2(ACCESS_TOKEN_JDOE)
			.body(new ApplicationDto(35500L))
			.when()
			.post("/assessments")
			.then()
			.log().all()
			.statusCode(201)
			.extract().as(AssessmentHeaderDto.class);

		// Retrieval of the assessment created
		AssessmentDto assessment = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON).auth().oauth2(ACCESS_TOKEN_JDOE)
			.when()
			.get("/assessments/" + header.getId())
			.then()
			.statusCode(200)
			.extract().as(AssessmentDto.class);

		assertThat(assessment.getQuestionnaire().getCategories().stream().allMatch(a -> a.getTitle().startsWith("CA:"))).isTrue();
		assertThat(assessment.getQuestionnaire().getCategories().stream().allMatch(a -> a.getQuestions().stream().allMatch (b -> b.getQuestion().startsWith("CA:")))).isTrue();
		assertThat(assessment.getQuestionnaire().getCategories().stream().allMatch(a -> a.getQuestions().stream().allMatch (b -> b.getDescription().startsWith("CA:")))).isTrue();
		assertThat(assessment.getQuestionnaire().getCategories().stream().allMatch(a -> a.getQuestions().stream().allMatch (b -> b.getOptions().stream().allMatch(c -> c.getOption().startsWith("CA:"))))).isTrue();
	}
}
