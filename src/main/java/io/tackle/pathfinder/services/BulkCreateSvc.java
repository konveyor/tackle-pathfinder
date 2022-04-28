/*
 * Copyright © 2021 Konveyor (https://konveyor.io/)
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
package io.tackle.pathfinder.services;

import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.bulk.AssessmentBulk;
import io.tackle.pathfinder.model.bulk.AssessmentBulkApplication;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Log
public class BulkCreateSvc {

    @Inject
    AssessmentSvc assessmentSvc;

    @Transactional(dontRollbackOn = {BadRequestException.class, NotFoundException.class})
    public void processBulkApplications(AssessmentBulk bulk) {
        bulk.bulkApplications.forEach(app -> {
            String error = null;
            try {
                // Delete current assessment if exists
                Optional<Assessment> currentAssessment = Assessment.find("applicationId", app.applicationId).firstResultOptional();
                currentAssessment.ifPresent(assessment -> assessmentSvc.deleteAssessment(assessment.id));

                // Copy the assessment from fromAssessmentId to applicationId
                Assessment assessment = AssessmentCreateCommand.builder()
                                            .applicationId(app.applicationId)
                                            .fromAssessmentId(bulk.fromAssessmentId)
                                            .username(bulk.createUser)
                                            .build().execute();
                app.assessmentId = assessment.id;
            } catch (BadRequestException ex) {
                error = "400";

            } catch (NotFoundException ex) {
                error = "404";
            }
            app.error = error;
            app.updateUser = bulk.createUser;
        });
        bulk.completed = true;
        bulk.updateUser = bulk.createUser;
    }

    @Transactional
    public AssessmentBulk newAssessmentBulk(Long fromAssessmentId, @NotNull @Valid List<Long> appList, String username) {
        AssessmentBulk bulk = AssessmentBulk.builder()
                            .createUser(username)
                            .fromAssessmentId(fromAssessmentId)
                            .build();

        appList.forEach(e -> {
            AssessmentBulkApplication bulkApplication = AssessmentBulkApplication.builder()
            .applicationId(e)
            .createUser(username)
            .assessmentBulk(bulk)
            .build();

            bulk.bulkApplications.add(bulkApplication);
        });
        bulk.persistAndFlush();
        log.info("Number of apps within bulk: " + bulk.bulkApplications.size());
        return bulk;
    }
}
