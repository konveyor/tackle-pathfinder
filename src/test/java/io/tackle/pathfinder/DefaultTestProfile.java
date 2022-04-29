/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
