package io.tackle.pathfinder.controllers.impl;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(value = PostgreSQLDatabaseTestResource.class,
        initArgs = {
                @ResourceArg(name = PostgreSQLDatabaseTestResource.DB_NAME, value = "pathfinder_db"),
                @ResourceArg(name = PostgreSQLDatabaseTestResource.USER, value = "pathfinder"),
                @ResourceArg(name = PostgreSQLDatabaseTestResource.PASSWORD, value = "pathfinder")
        }
)
public class AssessmentsResourceImplTest {

	@Test
	public void testGetApplicationAssessments() {
		given()
		.when().get("/applications/20/assessments")
		.then()
		   .statusCode(200)
		   .body("id", is(1),
		         "applicationId", is(20),
				 "status", is("STARTED"));
  	}	
	  
	@Test
	public void testGetApplicationAssessmentsNotFound() {
		given()
		.when().get("/applications/30/assessments")
		.then()
		   .statusCode(404);
  	}
}
