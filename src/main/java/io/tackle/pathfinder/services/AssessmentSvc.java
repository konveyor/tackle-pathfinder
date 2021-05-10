package io.tackle.pathfinder.services;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.tackle.pathfinder.dto.AssessmentBulkDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
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

@ApplicationScoped
@Log
public class AssessmentSvc {
    @Inject
    AssessmentMapper mapper;

    @Inject
    EventBus eventBus;

    @Inject
    SecurityIdentity identityContext;

    @Inject 
    UserTransaction transaction;

    @Inject
    BulkCreateSvc bulkSvc;


    @Transactional
    public Optional<AssessmentHeaderDto> getAssessmentHeaderDtoByApplicationId(@NotNull Long applicationId) {
        return Assessment.find("application_id", applicationId)
                        .firstResultOptional()
                        .map(e -> mapper.assessmentToAssessmentHeaderDto((Assessment) e));
    }


    @Transactional
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
                    log.log(Level.FINE, "stakegroup : " + stakegroup);
                }
            });
            assessment.stakeholdergroups.removeIf(e -> assessmentDto.getStakeholderGroups().stream().noneMatch(f -> f.equals(e.stakeholdergroupId)));

            // Add not existing stakeholdergroups included in the current array
            assessmentDto.getStakeholderGroups().forEach(e -> {
                log.log(Level.FINE, "Considering Stakeholdergroup : " + e);
                if (assessment.stakeholdergroups.stream().noneMatch(o -> e.equals(o.stakeholdergroupId))) {
                    log.log(Level.FINE,"Adding Stakeholdergroup : " + e);
                    assessment.stakeholdergroups.add(AssessmentStakeholdergroup.builder()
                            .assessment(assessment)
                            .stakeholdergroupId(e)
                            .build());
                }
            });
        }
        if (null != assessmentDto.getStakeholders()) {
            // Delete existing stakeholders not included in current array
            assessment.stakeholders.forEach(stake -> {
                if (assessmentDto.getStakeholders().stream().noneMatch(f -> f.equals(stake.stakeholderId))) {
                    log.log(Level.FINE,"Deleted stake : " + stake.stakeholderId);
                    stake.delete();
                }
            });
            assessment.stakeholders.removeIf(e -> assessmentDto.getStakeholders().stream().noneMatch(f -> f.equals(e.stakeholderId)));

            // Add not existing stakeholders included in the current array
            assessmentDto.getStakeholders().forEach(e -> {
                log.log(Level.FINE,"Considering Stakeholder : " + e);
                if (assessment.stakeholders.stream().noneMatch(o -> e.equals(o.stakeholderId))) {
                    log.log(Level.FINE,"Adding Stakeholder : " + e);
                    assessment.stakeholders.add(AssessmentStakeholder.builder()
                        .assessment(assessment)
                        .stakeholderId(e)
                        .build());
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
    public AssessmentHeaderDto newAssessment(Long fromAssessmentId, @NotNull @Valid Long applicationId) {
        Assessment assessment = AssessmentCreateCommand.builder()
            .applicationId(applicationId)
            .fromAssessmentId(fromAssessmentId)
            .username(identityContext.getPrincipal().getName())
        .build()
        .execute();
        return mapper.assessmentToAssessmentHeaderDto(assessment);
    }

    @Transactional
    public void deleteAssessment(@NotNull Long assessmentId) {
        Assessment assessment = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);
        boolean deleted = Assessment.deleteById(assessment.id);
        log.log(Level.FINE, "Deleted assessment : " + assessmentId + " = " + deleted);
        if (!deleted) throw new BadRequestException();
    }
 
    public AssessmentBulkDto bulkCreateAssessments(Long fromAssessmentId, @NotNull @Valid List<Long> appList) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        // We manage manually the transaction to be sure the consumer starts after this transaction has been commited
        transaction.begin();
        AssessmentBulk bulkNew = bulkSvc.newAssessmentBulk(fromAssessmentId, appList, identityContext.getPrincipal().getName());
        transaction.commit();

        eventBus.send("process-bulk-assessment-creation", bulkNew.id);

        log.info("Finishing request");
        return mapper.assessmentBulkToassessmentBulkDto(bulkNew);
    }

    @Transactional
    @ConsumeEvent("process-bulk-assessment-creation")
    @Blocking
    public void processApplicationAssessmentCreationAsync(Long bulkId) {
        log.log(Level.FINE, "Starting async process");

        AssessmentBulk bulk = (AssessmentBulk) AssessmentBulk.findByIdOptional(bulkId).orElseThrow(NotFoundException::new);
        log.log(Level.FINE, "Bulk : " + bulk.id);
        bulkSvc.processBulkApplications(bulk);
    }

    @Transactional
    public AssessmentBulkDto bulkGet(@NotNull Long bulkId) {
        AssessmentBulk bulk = (AssessmentBulk) AssessmentBulk.findByIdOptional(bulkId).orElseThrow(NotFoundException::new);

        return mapper.assessmentBulkToassessmentBulkDto(bulk);
	}

}
