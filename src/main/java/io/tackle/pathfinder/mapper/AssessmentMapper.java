package io.tackle.pathfinder.mapper;

import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.model.assessment.Assessment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface AssessmentMapper {

    AssessmentHeaderDto assessmentToAssessmentHeaderDto(Assessment assessment);

    
    AssessmentDto assessmentToAssessmentDto(Assessment assessment);
}
