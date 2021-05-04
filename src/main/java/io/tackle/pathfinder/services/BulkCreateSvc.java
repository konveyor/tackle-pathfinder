package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.bulk.AssessmentBulk;
import io.tackle.pathfinder.model.bulk.AssessmentBulkApplication;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class BulkCreateSvc {
    Long fromAssessmentId;
    Long bulkId;

    @Transactional(dontRollbackOn = {BadRequestException.class, NotFoundException.class} )
    public void processBulkApplications(AssessmentBulk bulk) {
        bulk.bulkApplications.forEach( app -> {
            String error = null;

            try {
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
            app.persist();

        });
        bulk.completed = true;
        bulk.updateUser = bulk.createUser;
    }

    @Transactional
    public AssessmentBulk newAssessmentBulk(Long fromAssessmentId, @NotNull @Valid List<Long> appList, String username) {
        AssessmentBulk bulk = AssessmentBulk.builder()
                            .applications(StringUtils.join(appList, ","))
                            .createUser(username)
                            .fromAssessmentId(fromAssessmentId)
                            .build();

        appList.stream().forEach(e -> {
            AssessmentBulkApplication bulkApplication = AssessmentBulkApplication.builder()
            .applicationId(e)
            .createUser(username)
            .assessmentBulk(bulk)
            .build();

            bulk.bulkApplications.add(bulkApplication);
        });
        bulk.persistAndFlush();

        return bulk;
    }
}
