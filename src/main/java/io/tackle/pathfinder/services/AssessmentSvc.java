package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.mapper.AssessmentMapper;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.Test;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
@Log
public class AssessmentSvc {
    @Inject
    AssessmentMapper mapper;

    public Optional<AssessmentHeaderDto> getAssessmentHeaderDtoByApplicationId(@NotNull Long applicationId) {
        List<Assessment> assessmentQuery = Assessment.list("application_id", applicationId);
        return assessmentQuery.stream().findFirst().map(e -> mapper.assessmentToAssessmentHeaderDto(e));
    }

    @Transactional
    public AssessmentHeaderDto createAssessment(@NotNull Long applicationId) {
        long count = Assessment.count("application_id", applicationId);
        log.info("Assessments for application_id [ " + applicationId + "] : " + count);
        if (count == 0) {
            Assessment assessment = new Assessment();
            assessment.applicationId = applicationId;
            assessment.status = AssessmentStatus.STARTED;
            assessment.persist();

            copyQuestionnaireIntoAssessment(assessment.id, defaultQuestionnaire());

            return mapper.assessmentToAssessmentHeaderDto(assessment);
        } else {
            throw new BadRequestException();
        }
    }

    private void copyQuestionnaireIntoAssessment(Long assessmentId, Long defaultQuestionnaireId) {
        Test.findAll().firstResult().
    }

    private Long defaultQuestionnaire() {
        Stream<Questionnaire> stream = Questionnaire.streamAll();
         Questionnaire first = stream.findFirst().get();
         return first.id;
    }

}
