package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.mapper.AssessmentMapper;
import io.tackle.pathfinder.model.Risk;
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
import lombok.Value;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
@Log
public class AssessmentSvc {
    @Inject
    AssessmentMapper mapper;

    @Inject
    EntityManager entityManager;

    public Optional<AssessmentHeaderDto> getAssessmentHeaderDtoByApplicationId(@NotNull Long applicationId) {
        List<Assessment> assessmentQuery = Assessment.list("application_id", applicationId);
        return assessmentQuery.stream().findFirst().map(e -> mapper.assessmentToAssessmentHeaderDto(e));
    }

    @Transactional
    public AssessmentHeaderDto createAssessment(@NotNull Long applicationId) {
        long count = Assessment.count("application_id", applicationId);
        log.log(Level.FINE, "Assessments for application_id [ " + applicationId + "] : " + count);
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
                .questionnaire(assessment.assessmentQuestionnaire)
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
        log.log(Level.FINE, "Requesting Assessment " + assessmentId);
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
                if (assessmentDto.getStakeholderGroups().stream().noneMatch(f -> f.equals(stakegroup.stakeholdergroupId))) {
                    log.log(Level.FINE, "Deleted stakegroup : " + stakegroup.stakeholdergroupId);
                    stakegroup.delete();
                }
            });
            assessment.stakeholdergroups.removeIf(e -> assessmentDto.getStakeholderGroups().stream().noneMatch(f -> f.equals(e.stakeholdergroupId)));

            // Add not existing stakeholdergroups included in the current array
            assessmentDto.getStakeholderGroups().forEach(e -> {
                log.log(Level.FINE, "Considering Stakeholdergroup : " + e);
                if (assessment.stakeholdergroups.stream().noneMatch(o -> e.equals(o.stakeholdergroupId))) {
                    log.log(Level.FINE, "Adding Stakeholdergroup : " + e);
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
                    log.log(Level.FINE, "Deleted stake : " + stake.stakeholderId);
                    stake.delete();
                }
            });
            assessment.stakeholders.removeIf(e -> assessmentDto.getStakeholders().stream().noneMatch(f -> f.equals(e.stakeholderId)));

            // Add not existing stakeholders included in the current array
            assessmentDto.getStakeholders().forEach(e -> {
                log.log(Level.FINE, "Considering Stakeholder : " + e);
                if (assessment.stakeholders.stream().noneMatch(o -> e.equals(o.stakeholderId))) {
                    log.log(Level.FINE, "Adding Stakeholder : " + e);
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
    public void deleteAssessment(@NotNull Long assessmentId) {
        Assessment assessment = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);
        boolean deleted = Assessment.deleteById(assessment.id);
        log.log(Level.FINE, "Deleted assessment : " + assessmentId + " = " + deleted);
        if (!deleted) throw new BadRequestException();
    }

    @Transactional
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
                }).collect(toList());
                assessmentTarget.stakeholders = assessmentSource.stakeholders.stream().map(e -> {
                    AssessmentStakeholder stakeholder = AssessmentStakeholder.builder()
                        .assessment(assessmentTarget)
                        .stakeholderId(e.stakeholderId)
                        .build();
                    stakeholder.persist();
                    return stakeholder;
                }).collect(toList());
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
                }).collect(toList());
                return assessmentQuestion;
            }).collect(toList());
            return assessmentCategory;
        }).collect(toList());

        return questionnaire;
    }

    @Transactional
    public List<LandscapeDto> landscape(List<Long> applicationIds) {
            String sql = "SELECT cast(ID as int), RISK, cast(trunc(max(PCT)) as int) AS PERCENTAGE " +
                " FROM ( " +
                "    SELECT assess.id, " +
                "            so.risk, " +
                "            trunc(((0.0 + Count(*) OVER w_risk_count) / (Count(*) OVER (PARTITION BY assess.id)) * 100)) AS pct " +
                "            FROM assessment_singleoption so " +
                "                    join assessment_question qu on (qu.id = so.assessment_question_id and qu.deleted is not true) " +
                "                    join assessment_category ca on (ca.id = qu.assessment_category_id and ca..deleted is not true) " +
                "                    join assessment_questionnaire ques on (ques.id = ca.assessment_questionnaire_id and ques..deleted is not true) " +
                "                    join assessment assess on (assess.id = ques.assessment_id and assess.deleted is not true) " +
                "            WHERE so.selected = true " +
                "                   AND assess.application_id in (" + applicationIds.stream().map(e -> e.toString()).collect(Collectors.joining(",")) + ") " +
                "                   AND assess.status = 'COMPLETE' " +
                "            window w_risk_count as (partition by assess.id, so.risk) " +
                "    ) AS risks " +
                " GROUP BY ID, risk;";
            Query query = entityManager.createNativeQuery(sql);

            List<AssessmentRiskDto> resultMappedToAssessmentsRisk = (List<AssessmentRiskDto>) query.getResultList().stream()
                .map(this::sqlRowToAssessmentRisk)
                .collect(Collectors.toList());

            List<LandscapeDto> collect = resultMappedToAssessmentsRisk.stream()
                .collect(Collectors.groupingBy(e -> e.id, Collectors.mapping(Function.identity(), Collectors.maxBy(this::compareAssessmentRisk))))
                .values().stream()
                .map(a -> new LandscapeDto(a.get().getId().longValue(), "UNKNOWN".equalsIgnoreCase(a.get().getRisk()) ? Risk.GREEN : Risk.valueOf(a.get().getRisk())))
                .collect(toList());
            return collect;
    }
    @Transactional
    public List<RiskLineDto> identifiedRisks(List<Long> applicationList) {
        String sqlString = "select cat.category_order, cat.name, q.question_order, q.question_text, opt.singleoption_order, opt.option, cast(array_agg(a.application_id) as text) \n" +
                "from assessment_category cat join assessment_question q on cat.id = q.assessment_category_id\n" +
                "                             join assessment_singleoption opt on q.id = opt.assessment_question_id and opt.selected is true\n" +
                "                             join assessment_questionnaire aq on cat.assessment_questionnaire_id = aq.id\n" +
                "                             join assessment a on aq.assessment_id = a.id\n" +
                "where cat.deleted is not true\n" +
                "      AND q.deleted is not true\n" +
                "      AND opt.deleted is not true\n" +
                "      AND aq.deleted is not true\n" +
                "      AND a.deleted is not true\n" +
                "      AND a.application_id in (" + StringUtils.join(applicationList, ",") + ") " +
                "group by cat.category_order, cat.name, q.question_order, q.question_text, opt.singleoption_order, opt.option " +
                "order by cat.category_order, q.question_order, opt.singleoption_order;";

        Query query = entityManager.createNativeQuery(sqlString);
        return mapper.riskListQueryToRiskLineDtoList(query.getResultList());
    }

    private AssessmentRiskDto sqlRowToAssessmentRisk(Object row) {
        return new AssessmentRiskDto((Integer) ((Object[]) row)[0], (String) ((Object[]) row)[1], (Integer) ((Object[]) row)[2]);
    }

    private int compareAssessmentRisk(AssessmentRiskDto o1, AssessmentRiskDto o2) {
        if ("RED".equalsIgnoreCase(o1.risk) && o1.percentage > 0) return 1;
        else if ("RED".equalsIgnoreCase(o2.risk) && o2.percentage > 0) return -1;
        else if ("AMBER".equalsIgnoreCase(o1.risk) && o1.percentage > 30) return 1;
        else if ("AMBER".equalsIgnoreCase(o2.risk) && o2.percentage > 30) return -1;
        else if ("GREEN".equalsIgnoreCase(o1.risk)) return 1;
        else if ("GREEN".equalsIgnoreCase(o2.risk)) return -1;
        else if ("UNKNOWN".equalsIgnoreCase(o1.risk)) return 1;
        else if ("UNKNOWN".equalsIgnoreCase(o2.risk)) return -1;
        else return 0; // this should not happen
    }


    @Value
    public class AssessmentRiskDto {
        Integer id;
        String risk;
        Integer percentage;
    }
}