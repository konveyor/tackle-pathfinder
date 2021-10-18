package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentQuestion;
import io.tackle.pathfinder.model.assessment.AssessmentQuestionnaire;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import lombok.Builder;
import lombok.extern.java.Log;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Log
@Builder
public class AssessmentCreateCommand {
    private Long fromAssessmentId;
    private String username;
    private Long applicationId;
    private Long questionnaireId;

    private Assessment createAssessment(@NotNull Long applicationId) {
        if (Assessment.find("application_id", applicationId).firstResultOptional().isPresent()) {
            throw new BadRequestException();
        }
        else {
            Assessment assessment = new Assessment();
            assessment.applicationId = applicationId;
            assessment.status = AssessmentStatus.STARTED;
            assessment.createUser = username;

            copyQuestionnaireIntoAssessment(assessment, getQuestionnaireDefaulted(questionnaireId));
            assessment.persist();
            return assessment;
        }
    }

    private Assessment copyQuestionnaireIntoAssessment(Assessment assessment, Questionnaire questionnaire) {

        AssessmentQuestionnaire assessQuestionnaire = AssessmentQuestionnaire.builder()
                .name(questionnaire.name)
                .questionnaire(questionnaire)
                .assessment(assessment)
                .languageCode(questionnaire.languageCode)
                .createUser(username)
                .build();

        assessment.assessmentQuestionnaire = assessQuestionnaire;

        for (Category category : questionnaire.categories) {
            AssessmentCategory assessmentCategory = AssessmentCategory.builder()
                    .name(category.name)
                    .order(category.order)
                    .questionnaire(assessment.assessmentQuestionnaire )
                    .questionnaire_categoryId(category.id)
                    .createUser(username)
                    .build();

            for (Question question : category.questions) {
                AssessmentQuestion assessmentQuestion = AssessmentQuestion.builder()
                        .category(assessmentCategory)
                        .name(question.name)
                        .order(question.order)
                        .questionText(question.questionText)
                        .type(question.type)
                        .description(question.description)
                        .createUser(username)
                        .questionnaire_questionId(question.id)
                        .build();

                for (SingleOption so : question.singleOptions) {
                    AssessmentSingleOption singleOption = AssessmentSingleOption.builder()
                        .option(so.option)
                        .order(so.order)
                        .question(assessmentQuestion)
                        .risk(so.risk)
                        .selected(false)
                        .createUser(username)
                        .questionnaire_optionId(so.id)
                        .build();

                    assessmentQuestion.singleOptions.add(singleOption);
                }
                assessmentCategory.questions.add(assessmentQuestion);
            }
            assessQuestionnaire.categories.add(assessmentCategory);
        }

        return assessment;
    }

    private Questionnaire getQuestionnaireDefaulted(Long questionnaire) {
        log.log(Level.FINE, "questionnaires : " + Questionnaire.count());
        if (questionnaire != null) {
            return (Questionnaire) Questionnaire.findByIdOptional(questionnaire).orElseThrow(NotFoundException::new);
        } else {
            return (Questionnaire) Questionnaire.findAll().firstResultOptional().orElseThrow(NotFoundException::new);
        }
    }

    private Assessment copyAssessment(@NotNull Long assessmentId, @NotNull Long targetApplicationId) {
            Assessment assessmentSource = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);
            if (assessmentSource != null) {
                Optional<Assessment> currentAssessment = Assessment.find("applicationId", targetApplicationId).firstResultOptional();
                if (currentAssessment.isEmpty()) {
                    Assessment assessmentTarget = Assessment.builder()
                            .applicationId(targetApplicationId)
                            .status(assessmentSource.status)
                            .comment(assessmentSource.comment)
                            .createUser(username)
                            .build();
                    assessmentTarget.assessmentQuestionnaire = copyQuestionnaireBetweenAssessments(assessmentSource, assessmentTarget);

                    assessmentTarget.stakeholdergroups.addAll(assessmentSource.stakeholdergroups.stream().map(e -> AssessmentStakeholdergroup.builder()
                            .assessment(assessmentTarget)
                            .stakeholdergroupId(e.stakeholdergroupId)
                            .createUser(username)
                            .build()).collect(Collectors.toList()));

                    assessmentTarget.stakeholders.addAll(assessmentSource.stakeholders.stream().map(e -> AssessmentStakeholder.builder()
                            .assessment(assessmentTarget)
                            .stakeholderId(e.stakeholderId)
                            .createUser(username)
                            .build()).collect(Collectors.toList()));

                    assessmentTarget.persistAndFlush();
                    return assessmentTarget;
                } else {
                    throw new IllegalStateException("Could not copy assessment into application:" + targetApplicationId);
                }
        }

        throw new BadRequestException();
    }

    private AssessmentQuestionnaire copyQuestionnaireBetweenAssessments(Assessment sourceAssessment, Assessment targetAssessment) {
        AssessmentQuestionnaire questionnaire = AssessmentQuestionnaire.builder()
                                                .assessment(targetAssessment)
                                                .questionnaire(sourceAssessment.assessmentQuestionnaire.questionnaire)
                                                .name(sourceAssessment.assessmentQuestionnaire.name)
                                                .languageCode(sourceAssessment.assessmentQuestionnaire.languageCode)
                                                .createUser(username)
                                                .build();
        questionnaire.categories.addAll(sourceAssessment.assessmentQuestionnaire.categories.stream().map(cat -> {
            AssessmentCategory assessmentCategory = AssessmentCategory.builder()
                .comment(cat.comment)
                .name(cat.name)
                .order(cat.order)
                .questionnaire(questionnaire)
                .createUser(username)
                .questionnaire_categoryId(cat.questionnaire_categoryId)
                .build();
            assessmentCategory.questions.addAll(cat.questions.stream().map(que -> {
                AssessmentQuestion assessmentQuestion = AssessmentQuestion.builder()
                .category(assessmentCategory)
                .description(que.description)
                .name(que.name)
                .order(que.order)
                .questionText(que.questionText)
                .type(que.type)
                .createUser(username)
                .questionnaire_questionId(que.questionnaire_questionId)
                .build();
                assessmentQuestion.singleOptions.addAll(que.singleOptions.stream().map(opt -> {
                    AssessmentSingleOption singleOption = AssessmentSingleOption.builder()
                    .option(opt.option)
                    .order(opt.order)
                    .question(assessmentQuestion)
                    .risk(opt.risk)
                    .selected(opt.selected)
                    .createUser(username)
                    .questionnaire_optionId(opt.questionnaire_optionId)
                    .build();
                    return singleOption;
                }).collect(Collectors.toList()));
                return assessmentQuestion;
            }).collect(Collectors.toList()));
            return assessmentCategory;
        }).collect(Collectors.toList()));

        return questionnaire;
    }

    public Assessment execute() {
        if (fromAssessmentId != null) {
            return copyAssessment(fromAssessmentId, applicationId);
        } else {
            return createAssessment(applicationId);
        }
    }
}
