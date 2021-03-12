package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.mapper.AssessmentMapper;
import io.tackle.pathfinder.model.assessment.Assessment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;

@ApplicationScoped
public class AssessmentSvc {
    @Inject
    AssessmentMapper mapper;

    public AssessmentHeaderDto gAssessmentHeaderDtoByApplicationId(Long applicationId) {
        List<Assessment> assessmentQuery = Assessment.list("application_id", applicationId);
        System.out.println("xxxxxxxxxxxx" + assessmentQuery);
        return mapper.assessmentToAssessmentHeaderDto(assessmentQuery.get(0));
    }

}
