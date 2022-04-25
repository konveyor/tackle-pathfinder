package io.tackle.pathfinder;

import io.tackle.commons.testcontainers.KeycloakTestResource;

import java.util.List;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class DefaultTestProfile extends PathfinderTestProfile {

    protected TestResourceEntry kcResource = new TestResourceEntry(KeycloakTestResource.class, ofEntries(
            entry(KeycloakTestResource.IMPORT_REALM_JSON_PATH, "keycloak/quarkus-realm.json"),
            entry(KeycloakTestResource.REALM_NAME, "quarkus")
    ));

    @Override
    public List<TestResourceEntry> testResources() {
        return List.of(dbResource, kcResource);
    }

}
