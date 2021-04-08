package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.mapper.AssessmentMapper;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentQuestion;
import io.tackle.pathfinder.model.assessment.AssessmentQuestionnaire;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Log
public class AssessmentSvc {
    @Inject
    public AssessmentMapper mapper;

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

    @Transactional
    public Assessment copyQuestionnaireIntoAssessment(Long assessmentId, Long questionnaireId) {
        Questionnaire questionnaire = Questionnaire.findById(questionnaireId);
        Assessment assessment = Assessment.findById(assessmentId);

        AssessmentQuestionnaire assessQuestionnaire = AssessmentQuestionnaire.builder().name(questionnaire.name)
                .questionnaire(questionnaire).assessment(assessment).languageCode(questionnaire.languageCode).build();
        assessQuestionnaire.persist();

        assessment.assessmentQuestionnaire = assessQuestionnaire;

        assessment.assessmentQuestionnaire.categories = new ArrayList<AssessmentCategory>();
        for (Category category : questionnaire.categories) {
            AssessmentCategory assessmentCategory = AssessmentCategory.builder().name(category.name)
                    .order(category.order).questionnaire(assessment.assessmentQuestionnaire )
                    .questions(new ArrayList<>()).build();
            assessmentCategory.persist();

            for (Question question : category.questions) {
                AssessmentQuestion assessmentQuestion = AssessmentQuestion.builder().category(assessmentCategory)
                        .name(question.name).order(question.order).questionText(question.questionText).type("SINGLE")
                        .singleOptions(new ArrayList<>()).build();

                assessmentQuestion.persist();

                for (SingleOption so : question.singleOptions) {
                    AssessmentSingleOption singleOption = AssessmentSingleOption.builder().option(so.option)
                            .order(so.order).question(assessmentQuestion).risk(so.risk).selected(false).build();

                    singleOption.persist();

                    assessmentQuestion.singleOptions.add(singleOption);
                }
                assessmentCategory.questions.add(assessmentQuestion);
            }
            assessQuestionnaire.categories.add(assessmentCategory);
        }

        return assessment;
    }

    private Long defaultQuestionnaire() {
        log.info("questionnaires : " + Questionnaire.count());
        return Questionnaire.<Questionnaire>streamAll().findFirst().map(e -> e.id).orElseThrow();
    }

}
