package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.mapper.AssessmentMapper;
import io.tackle.pathfinder.model.assessment.Assessment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AssessmentSvc {
    @Inject
    AssessmentMapper mapper;

    public Optional<AssessmentHeaderDto> getAssessmentHeaderDtoByApplicationId(Long applicationId) {
        List<Assessment> assessmentQuery = Assessment.list("application_id", applicationId);
        return assessmentQuery.stream().findFirst().map(e -> mapper.assessmentToAssessmentHeaderDto(e));
    }

    @Transactional
    public AssessmentHeaderDto createAssessment(Long applicationId) {
        System.out.println("Count all : " + Assessment.count());
        long count = Assessment.count("application_id", applicationId);
        System.out.println("Count : " + count);
        if (count == 0) {
            Assessment assessment = new Assessment();
            assessment.applicationId = applicationId;
            assessment.status = AssessmentStatus.STARTED;
            assessment.persist();

            return mapper.assessmentToAssessmentHeaderDto(assessment);
        } else {
            throw new BadRequestException();
        }
    }

}
