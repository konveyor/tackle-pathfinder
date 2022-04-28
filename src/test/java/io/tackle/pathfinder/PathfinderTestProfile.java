package io.tackle.pathfinder;

import io.quarkus.test.junit.QuarkusTestProfile;
import io.tackle.commons.testcontainers.KeycloakTestResource;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public abstract class PathfinderTestProfile implements QuarkusTestProfile {

    protected TestResourceEntry dbResource = new TestResourceEntry(PostgreSQLDatabaseTestResource.class, ofEntries(
            entry(PostgreSQLDatabaseTestResource.DB_NAME, "pathfinder_db"),
            entry(PostgreSQLDatabaseTestResource.USER, "pathfinder"),
            entry(PostgreSQLDatabaseTestResource.PASSWORD, "pathfinder")
    ));

    @Override
    public List<TestResourceEntry> testResources() {
        return List.of(dbResource);
    }

    @Override
    public String getConfigProfile() {
        return "test";
    }
}
