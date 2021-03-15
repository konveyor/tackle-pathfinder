package io.tackle.pathfinder.controllers.impl;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat; // main one

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

		AssessmentHeaderDto[] assessments = given().queryParam("applicationId", "20").when().get("/assessments")
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
	public void testGetApplicationAssessmentsNotFound() {
		Object[] elements = given().queryParam("applicationId", "30").when().get("/assessments")
		.then()
			.statusCode(200)
			.extract()
			.as(Object[].class);
		assertThat(elements).isEmpty();
  	}
}
