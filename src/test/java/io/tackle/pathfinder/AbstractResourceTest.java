package io.tackle.pathfinder;

import io.restassured.RestAssured;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;

public abstract class AbstractResourceTest {

    protected static String PATH = "";
    protected static String ACCESS_TOKEN;

    @BeforeAll
    public static void setUp() {
        Config config = ConfigProvider.getConfig();
        Boolean oidcEnabled = config.getValue("quarkus.oidc.enabled", Boolean.class);
        if (oidcEnabled) {
            final String KEYCLOAK_SERVER_URL = ConfigProvider.getConfig().getOptionalValue("quarkus.oidc.auth-server-url", String.class).orElse("http://localhost:8180/auth");
            ACCESS_TOKEN = given()
                    .relaxedHTTPSValidation()
                    .auth().preemptive().basic("backend-service", "secret")
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("grant_type", "password")
                    .formParam("username", "alice")
                    .formParam("password", "alice")
                    .when()
                    .post(KEYCLOAK_SERVER_URL + "/protocol/openid-connect/token")
                    .then().extract().path("access_token").toString();
            RestAssured.authentication = oauth2(ACCESS_TOKEN);
        }
    }

    /**
     * Maybe too much to execute it every time a class extends this one but, right now,
     * the "better safe than sorry" approach with security is the winning one.
     */
    @Test
    public void testUnauthorized() {
        Config config = ConfigProvider.getConfig();
        Boolean authorizationDisabled = config.getValue("pathfinder.disable.authorization", Boolean.class);

        if (!authorizationDisabled) {
            given().auth().oauth2("")
                    .accept("application/hal+json")
                    .queryParam("sort", "id")
                    .when().get(PATH)
                    .then()
                    .statusCode(401);
        }
    }

}
