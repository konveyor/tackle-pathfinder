package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.mapper.AssessmentMapper;
import io.tackle.pathfinder.model.assessment.Assessment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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

}
