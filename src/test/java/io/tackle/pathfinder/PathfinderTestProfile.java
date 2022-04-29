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
