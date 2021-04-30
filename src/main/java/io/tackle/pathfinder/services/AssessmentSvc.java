package io.tackle.pathfinder.services;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.tackle.pathfinder.dto.AssessmentBulkDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
import io.tackle.pathfinder.mapper.AssessmentMapper;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentQuestion;
import io.tackle.pathfinder.model.assessment.AssessmentQuestionnaire;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import io.tackle.pathfinder.model.bulk.AssessmentBulk;
import io.tackle.pathfinder.model.bulk.AssessmentBulkApplication;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.context.ManagedExecutor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
public class AssessmentSvc {
    @Inject
    AssessmentMapper mapper;

    @Inject
    EventBus eventBus;

    @Inject 
    ManagedExecutor executor;

    @Inject
    SecurityIdentity identityContext;

    @Inject 
    UserTransaction transaction;

    public Optional<AssessmentHeaderDto> getAssessmentHeaderDtoByApplicationId(@NotNull Long applicationId) {
        List<Assessment> assessmentQuery = Assessment.list("application_id", applicationId);
        return assessmentQuery.stream().findFirst().map(e -> mapper.assessmentToAssessmentHeaderDto(e));
    }

    @Transactional(dontRollbackOn = {BadRequestException.class})

    public AssessmentHeaderDto createAssessment(@NotNull Long applicationId) {
        long count = Assessment.count("application_id", applicationId);
        if (count == 0) {
            Assessment assessment = new Assessment();
            assessment.applicationId = applicationId;
            assessment.status = AssessmentStatus.STARTED;
            assessment.persistAndFlush();

            copyQuestionnaireIntoAssessment(assessment, defaultQuestionnaire());

            return mapper.assessmentToAssessmentHeaderDto(assessment);
        } else {
            throw new BadRequestException();
        }
    }

    @Transactional

    public Assessment copyQuestionnaireIntoAssessment(Assessment assessment, Questionnaire questionnaire) {

        AssessmentQuestionnaire assessQuestionnaire = AssessmentQuestionnaire.builder()
                .name(questionnaire.name)
                .questionnaire(questionnaire)
                .assessment(assessment)
                .languageCode(questionnaire.languageCode)
                .build();
        assessQuestionnaire.persist();

        assessment.assessmentQuestionnaire = assessQuestionnaire;

        for (Category category : questionnaire.categories) {
            AssessmentCategory assessmentCategory = AssessmentCategory.builder()
                    .name(category.name)
                    .order(category.order)
                    .questionnaire(assessment.assessmentQuestionnaire )
                    .build();
            assessmentCategory.persist();

            for (Question question : category.questions) {
                AssessmentQuestion assessmentQuestion = AssessmentQuestion.builder()
                        .category(assessmentCategory)
                        .name(question.name)
                        .order(question.order)
                        .questionText(question.questionText)
                        .type(question.type)
                        .description(question.description)
                        .build();

                assessmentQuestion.persist();

                for (SingleOption so : question.singleOptions) {
                    AssessmentSingleOption singleOption = AssessmentSingleOption.builder()
                        .option(so.option)
                        .order(so.order)
                        .question(assessmentQuestion)
                        .risk(so.risk)
                        .selected(false)
                        .build();

                    singleOption.persist();

                    assessmentQuestion.singleOptions.add(singleOption);
                }
                assessmentCategory.questions.add(assessmentQuestion);
            }
            assessQuestionnaire.categories.add(assessmentCategory);
        }

        return assessment;
    }

    private Questionnaire defaultQuestionnaire() {
        log.log(Level.FINE, "questionnaires : " + Questionnaire.count());
        return Questionnaire.<Questionnaire>streamAll().findFirst().orElseThrow(NotFoundException::new);
    }

    public AssessmentDto getAssessmentDtoByAssessmentId(@NotNull Long assessmentId) {
        log.log(Level.FINE,"Requesting Assessment " + assessmentId);
        Assessment assessment = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);

        return mapper.assessmentToAssessmentDto(assessment);
    }

    @Transactional
    public AssessmentHeaderDto updateAssessment(@NotNull Long assessmentId, @NotNull @Valid AssessmentDto assessmentDto) {
        Assessment assessment = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);
        AssessmentQuestionnaire assessment_questionnaire = AssessmentQuestionnaire.find("assessment_id=?1", assessmentId).<AssessmentQuestionnaire>firstResultOptional().orElseThrow(BadRequestException::new);

        if (null != assessmentDto.getStatus()) {
            assessment.status = assessmentDto.getStatus();
        }

        if (null != assessmentDto.getStakeholderGroups()) {
            // Delete existing stakeholdergroups not included in current array
            assessment.stakeholdergroups.forEach(stakegroup -> {
                if (!assessmentDto.getStakeholderGroups().contains(stakegroup.stakeholdergroupId)) {
                    log.log(Level.FINE,"Deleted stakegroup : " + stakegroup.stakeholdergroupId);
                    stakegroup.delete();
                }
            });
            // Add not existing stakeholdergroups included in the current array
            assessmentDto.getStakeholderGroups().forEach(groupDto -> {
                log.log(Level.FINE, "Considering Stakeholdergroup : " + groupDto);
                if (assessment.stakeholdergroups.stream().noneMatch(groupDB -> groupDto.equals(groupDB.stakeholdergroupId))) {
                    log.log(Level.FINE,"Adding Stakeholdergroup : " + groupDto);
                    AssessmentStakeholdergroup.builder()
                            .assessment(assessment)
                            .stakeholdergroupId(groupDto)
                            .build().persist();
                }
            });
        }
        if (null != assessmentDto.getStakeholders()) {
            // Delete existing stakeholders not included in current array
            assessment.stakeholders.forEach(stake -> {
                if (!assessmentDto.getStakeholders().contains(stake.stakeholderId)) {
                    log.log(Level.FINE,"Deleted stake : " + stake.stakeholderId);
                    stake.delete();
                }
            });
            // Add not existing stakeholders included in the current array
            assessmentDto.getStakeholders().forEach(e -> {
                log.log(Level.FINE,"Considering Stakeholder : " + e);
                if (assessment.stakeholders.stream().noneMatch(o -> e.equals(o.stakeholderId))) {
                    log.log(Level.FINE,"Adding Stakeholder : " + e);
                    AssessmentStakeholder.builder()
                        .assessment(assessment)
                        .stakeholderId(e)
                        .build().persist();
                }
            });
        }

        if (assessmentDto.getQuestionnaire() != null && assessmentDto.getQuestionnaire().getCategories() != null) {
            assessmentDto.getQuestionnaire().getCategories().forEach(categ -> {
                AssessmentCategory category = AssessmentCategory.find("assessment_questionnaire_id=?1 and id=?2", assessment_questionnaire.id, categ.getId()).<AssessmentCategory>firstResultOptional().orElseThrow(BadRequestException::new);
                if (categ.getComment() != null) {
                category.comment = categ.getComment();
                    log.log(Level.FINE, "Setting category comment : " + category.comment);
                }

                if (categ.getQuestions() != null) {
                    categ.getQuestions().forEach(que -> {
                        AssessmentQuestion question = AssessmentQuestion.find("assessment_category_id=?1 and id=?2", categ.getId(), que.getId()).<AssessmentQuestion>firstResultOptional().orElseThrow(BadRequestException::new);

                        if (que.getOptions() != null) {
                            que.getOptions().forEach(opt -> {
                                AssessmentSingleOption option = AssessmentSingleOption.find("assessment_question_id=?1 and id=?2", question.id, opt.getId()).<AssessmentSingleOption>firstResultOptional().orElseThrow(BadRequestException::new);
                                if (opt.getChecked() != null) {
                                option.selected = opt.getChecked();
                                    log.log(Level.FINE, "Setting option checked : " + option.selected);
                                }
                            });
                        }
                    });
                }
            });
        }

        return mapper.assessmentToAssessmentHeaderDto(assessment);
    }

    @Transactional
    public void deleteAssessment(@NotNull Long assessmentId) {
        Assessment assessment = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);
        boolean deleted = Assessment.deleteById(assessment.id);
        log.log(Level.FINE, "Deleted assessment : " + assessmentId + " = " + deleted);
        if (!deleted) throw new BadRequestException();
    }

    @Transactional(dontRollbackOn = {BadRequestException.class, NotFoundException.class})
    public AssessmentHeaderDto copyAssessment(@NotNull Long assessmentId, @NotNull Long targetApplicationId) {
        Assessment assessmentSource = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);
        if (assessmentSource != null) {
            if (Assessment.find("applicationId", targetApplicationId).firstResultOptional().isEmpty()) {
                Assessment assessmentTarget = Assessment.builder()
                                                .applicationId(targetApplicationId)
                                                .status(assessmentSource.status)
                                                .build();
                assessmentTarget.persist();

                assessmentTarget.assessmentQuestionnaire = copyQuestionnaireBetweenAssessments(assessmentSource, assessmentTarget);

                assessmentTarget.stakeholdergroups = assessmentSource.stakeholdergroups.stream().map(e -> {
                    AssessmentStakeholdergroup stakeholdergroup = AssessmentStakeholdergroup.builder()
                        .assessment(assessmentTarget)
                        .stakeholdergroupId(e.stakeholdergroupId)
                        .build();
                    stakeholdergroup.persist();
                    return stakeholdergroup;
                    }).collect(Collectors.toList());
                assessmentTarget.stakeholders = assessmentSource.stakeholders.stream().map(e -> {
                    AssessmentStakeholder stakeholder = AssessmentStakeholder.builder()
                        .assessment(assessmentTarget)
                        .stakeholderId(e.stakeholderId)
                        .build();
                    stakeholder.persist();
                    return stakeholder;
                    }).collect(Collectors.toList());
                assessmentTarget.persist();
                return mapper.assessmentToAssessmentHeaderDto(assessmentTarget);
            }
        }

        throw new BadRequestException();
    }

    @Transactional
    private AssessmentQuestionnaire copyQuestionnaireBetweenAssessments(Assessment sourceAssessment, Assessment targetAssessment) {
        AssessmentQuestionnaire questionnaire = AssessmentQuestionnaire.builder()
                                                .assessment(targetAssessment)
                                                .questionnaire(sourceAssessment.assessmentQuestionnaire.questionnaire)
                                                .name(sourceAssessment.assessmentQuestionnaire.name)
                                                .languageCode(sourceAssessment.assessmentQuestionnaire.languageCode)
                                                .build();
        questionnaire.persist();
        questionnaire.categories = sourceAssessment.assessmentQuestionnaire.categories.stream().map(cat -> {
            AssessmentCategory assessmentCategory = AssessmentCategory.builder()
                .comment(cat.comment)
                .name(cat.name)
                .order(cat.order)
                .questionnaire(questionnaire)
                .build();
            assessmentCategory.persist();
            assessmentCategory.questions = cat.questions.stream().map(que -> {
                AssessmentQuestion assessmentQuestion = AssessmentQuestion.builder()
                .category(assessmentCategory)
                .description(que.description)
                .name(que.name)
                .order(que.order)
                .questionText(que.questionText)
                .type(que.type)
                .build();
                assessmentQuestion.persist();
                assessmentQuestion.singleOptions = que.singleOptions.stream().map(opt -> {
                    AssessmentSingleOption singleOption = AssessmentSingleOption.builder()
                    .option(opt.option)
                    .order(opt.order)
                    .question(assessmentQuestion)
                    .risk(opt.risk)
                    .selected(opt.selected)
                    .build();
                    singleOption.persist();
                    return singleOption; 
                }).collect(Collectors.toList());
                return assessmentQuestion;
            }).collect(Collectors.toList());
            return assessmentCategory;
        }).collect(Collectors.toList());

        return questionnaire;
    }

    @Transactional(dontRollbackOn = {BadRequestException.class, NotFoundException.class} )
    public AssessmentHeaderDto newAssessment(Long fromAssessmentId, Long applicationId) {
        if (fromAssessmentId != null) {
            log.info("copying assess");
            return copyAssessment(fromAssessmentId, applicationId);
        } else {
            log.info("Creating new");
            return createAssessment(applicationId);
        }
    }

    public AssessmentBulkDto bulkCreateAssessments(Long fromAssessmentId, @NotNull @Valid List<Long> data) throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {
        // We manage manually the transaction to be sure the consumer starts after this transaction has been commited
        transaction.begin();
        AssessmentBulk bulk = newAssessmentBulk(fromAssessmentId, data);
        transaction.commit();

        eventBus.send("process-bulk-assessment-creation", BulkOperation.builder().bulkId(bulk.id).username(identityContext.getPrincipal().getName()).build());

        return mapper.assessmentBulkToassessmentBulkDto(bulk);
    }

    @Transactional
    @ConsumeEvent("process-bulk-assessment-creation")
    @Blocking
    public void processApplicationAssessmentCreationAsync(BulkOperation data) {
        AssessmentBulk bulk = (AssessmentBulk) AssessmentBulk.findByIdOptional(data.bulkId).orElseThrow(NotFoundException::new);
        processBulkApplications(data, bulk);
    }

    @Transactional(dontRollbackOn = {BadRequestException.class, NotFoundException.class} )
    private void processBulkApplications(BulkOperation data, AssessmentBulk bulk) {
        bulk.bulkApplications.forEach( app -> {
            String error = "none";
            AssessmentBulkApplication bulkApp = AssessmentBulkApplication.findById(app.id);
            try {
                AssessmentHeaderDto headerDto = newAssessment(bulk.fromAssessmentId, app.applicationId);
                bulkApp.assessmentId = headerDto.getId();
                bulkApp.updateUser = data.username;
            } catch (BadRequestException ex) {
                error = "400";
            } catch (NotFoundException ex) {
                error = "404";
            }
            bulkApp.error = error;
            bulkApp.persist();

            log.info("bulkcreateassessments - finished");
        });
    }

    @Transactional
    private AssessmentBulk newAssessmentBulk(Long fromAssessmentId, @NotNull @Valid List<Long> data) {

        AssessmentBulk bulk = AssessmentBulk.builder()
                .applications(StringUtils.join(data, ","))
                            .fromAssessmentId(fromAssessmentId)
                            .build();
        bulk.persistAndFlush();

        data.stream().forEach(e -> {
            AssessmentBulkApplication bulkApplication = AssessmentBulkApplication.builder()
            .applicationId(e)
            .createUser(identityContext.getPrincipal().getName())
            .assessmentBulk(bulk)
            .build();

            bulkApplication.persistAndFlush();

            bulk.bulkApplications.add(bulkApplication);
        });

        return bulk;
    }

    @Transactional
    public AssessmentBulkDto bulkGet(@NotNull Long bulkId) {
        AssessmentBulk bulk = (AssessmentBulk) AssessmentBulk.findByIdOptional(bulkId).orElseThrow(NotFoundException::new);

        return mapper.assessmentBulkToassessmentBulkDto(bulk);
	}

}
