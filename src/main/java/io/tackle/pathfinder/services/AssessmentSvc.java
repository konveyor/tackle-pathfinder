package io.tackle.pathfinder.services;

import com.google.common.util.concurrent.AtomicDouble;
import io.tackle.pathfinder.dto.*;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.tackle.pathfinder.dto.AssessmentBulkDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
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
import io.tackle.pathfinder.model.bulk.AssessmentBulk;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.apache.commons.lang3.StringUtils;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
@Log
public class AssessmentSvc {
    @Inject
    AssessmentMapper assessmentMapper;

    @Inject
    EntityManager entityManager;

    @ConfigProperty(name = "confidence.risk.RED.weight")
    Integer redWeight;
    @ConfigProperty(name = "confidence.risk.GREEN.weight")
    Integer greenWeight;
    @ConfigProperty(name = "confidence.risk.AMBER.weight")
    Integer amberWeight;
    @ConfigProperty(name = "confidence.risk.UNKNOWN.weight")
    Integer unknownWeight;

    @ConfigProperty(name = "confidence.risk.AMBER.multiplier")
    Double amberMultiplier;
    @ConfigProperty(name = "confidence.risk.RED.multiplier")
    Double redMultiplier;

    @ConfigProperty(name = "confidence.risk.RED.adjuster")
    Double redAdjuster;
    @ConfigProperty(name = "confidence.risk.AMBER.adjuster")
    Double amberAdjuster;
    @ConfigProperty(name = "confidence.risk.GREEN.adjuster")
    Double greenAdjuster;
    @ConfigProperty(name = "confidence.risk.UNKNOWN.adjuster")
    Double unknownAdjuster;

    @Inject
    EventBus eventBus;

    @Inject
    SecurityIdentity identityContext;

    @Inject
    UserTransaction transaction;

    @Inject
    BulkCreateSvc bulkSvc;

    @Transactional
    public AssessmentHeaderDto createAssessment(@NotNull Long applicationId) {
        return createAssessment(applicationId, null);
    }

    @Transactional
    public AssessmentHeaderDto createAssessment(@NotNull Long applicationId, Long questionnaireId) {
        long count = Assessment.count("application_id", applicationId);
        log.log(Level.FINE, "Assessments for application_id [ " + applicationId + "] : " + count);
        if (count == 0) {
            Assessment assessment = new Assessment();
            assessment.applicationId = applicationId;
            assessment.status = AssessmentStatus.STARTED;
            assessment.persistAndFlush();

            Questionnaire questionnaire = questionnaireId != null ? Questionnaire.findById(questionnaireId) : defaultQuestionnaire();
            copyQuestionnaireIntoAssessment(assessment,  questionnaire);

            return assessmentMapper.assessmentToAssessmentHeaderDto(assessment);
        } else {
            throw new BadRequestException();
        }
    }

    public Optional<AssessmentHeaderDto> getAssessmentHeaderDtoByApplicationId(@NotNull Long applicationId) {
        return Assessment.find("application_id", applicationId)
                        .firstResultOptional()
                        .map(e -> assessmentMapper.assessmentToAssessmentHeaderDto((Assessment) e));
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
                .questionnaire_categoryId(category.id)
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
                    .questionnaire_questionId(question.id)
                    .build();

                assessmentQuestion.persist();

                for (SingleOption so : question.singleOptions) {
                    AssessmentSingleOption singleOption = AssessmentSingleOption.builder()
                        .option(so.option)
                        .order(so.order)
                        .question(assessmentQuestion)
                        .risk(so.risk)
                        .selected(false)
                        .questionnaire_optionId(so.id)
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

    public AssessmentDto getAssessmentDtoByAssessmentId(@NotNull Long assessmentId, String language) {
        log.log(Level.FINE, "Requesting Assessment " + assessmentId + " in language " + language);
        Assessment assessment = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);

        return assessmentMapper.assessmentToAssessmentDto(assessment, StringUtils.defaultString(language));
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
                    category.updateUser = identityContext.getPrincipal().getName();
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
                                    option.updateUser = identityContext.getPrincipal().getName();
                                    log.log(Level.FINE, "Setting option checked : " + option.selected);
                                }
                            });
                        }
                    });
                }
            });
        }
        assessment.updateUser = identityContext.getPrincipal().getName();
        return assessmentMapper.assessmentToAssessmentHeaderDto(assessment);
    }

    @Transactional
    public AssessmentHeaderDto newAssessment(Long fromAssessmentId, @NotNull @Valid Long applicationId) {
        Assessment assessment = AssessmentCreateCommand.builder()
            .applicationId(applicationId)
            .fromAssessmentId(fromAssessmentId)
            .username(identityContext.getPrincipal().getName())
        .build()
        .execute();
        return assessmentMapper.assessmentToAssessmentHeaderDto(assessment);
    }

    @Transactional
    public void deleteAssessment(@NotNull Long assessmentId) {
        Assessment assessment = (Assessment) Assessment.findByIdOptional(assessmentId).orElseThrow(NotFoundException::new);
        boolean deleted = Assessment.deleteById(assessment.id);
        log.log(Level.FINE, "Deleted assessment : " + assessmentId + " = " + deleted);
        if (!deleted) throw new BadRequestException();
    }

    public AssessmentBulkDto bulkCreateAssessments(Long fromAssessmentId, @NotNull @Valid List<Long> appList) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        // We manage manually the transaction to be sure the consumer starts after this transaction has been committed
        transaction.begin();
        AssessmentBulk bulkNew = bulkSvc.newAssessmentBulk(fromAssessmentId, appList, identityContext.getPrincipal().getName());
        transaction.commit();

        eventBus.send("process-bulk-assessment-creation", bulkNew.id);
        log.info("Bulk scheduled");

        return assessmentMapper.assessmentBulkToAssessmentBulkDto(bulkNew);
    }

    @Transactional
    @ConsumeEvent("process-bulk-assessment-creation")
    @Blocking
    public void processApplicationAssessmentCreationAsync(Long bulkId) {
        log.log(Level.FINE, "Starting bulk copy async process");

        AssessmentBulk bulk = (AssessmentBulk) AssessmentBulk.findByIdOptional(bulkId).orElseThrow(NotFoundException::new);
        log.log(Level.FINE, "Bulk: " + bulk.id);
        bulkSvc.processBulkApplications(bulk);
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
                return assessmentMapper.assessmentToAssessmentHeaderDto(assessmentTarget);
            }
        }

        throw new BadRequestException();
    }

    @Transactional
    protected AssessmentQuestionnaire copyQuestionnaireBetweenAssessments(Assessment sourceAssessment, Assessment targetAssessment) {
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
                .questionnaire_categoryId(cat.questionnaire_categoryId)
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
                    .questionnaire_questionId(que.questionnaire_questionId)
                    .build();
                assessmentQuestion.persist();
                assessmentQuestion.singleOptions = que.singleOptions.stream().map(opt -> {
                    AssessmentSingleOption singleOption = AssessmentSingleOption.builder()
                        .option(opt.option)
                        .order(opt.order)
                        .question(assessmentQuestion)
                        .risk(opt.risk)
                        .selected(opt.selected)
                        .questionnaire_optionId(opt.questionnaire_optionId)
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
    public AssessmentBulkDto bulkGet(@NotNull Long bulkId) {
        AssessmentBulk bulk = (AssessmentBulk) AssessmentBulk.findByIdOptional(bulkId).orElseThrow(NotFoundException::new);

        return assessmentMapper.assessmentBulkToAssessmentBulkDto(bulk);
    }

    @Transactional
    public List<LandscapeDto> landscape(List<Long> applicationIds) {
            String sql = "SELECT ID, RISK, cast(trunc(max(PCT)) as int) AS PERCENTAGE , application_id " +
                " FROM ( " +
                "    SELECT assess.id, " +
                "            so.risk, " +
                "            trunc(((0.0 + Count(*) OVER w_risk_count) / (Count(*) OVER (PARTITION BY assess.id)) * 100)) AS pct, " +
                "            assess.application_id " +
                "            FROM assessment_singleoption so " +
                "                    join assessment_question qu on (qu.id = so.assessment_question_id and qu.deleted is not true) " +
                "                    join assessment_category ca on (ca.id = qu.assessment_category_id and ca.deleted is not true) " +
                "                    join assessment_questionnaire ques on (ques.id = ca.assessment_questionnaire_id and ques.deleted is not true) " +
                "                    join assessment assess on (assess.id = ques.assessment_id and assess.deleted is not true) " +
                "            WHERE so.selected = true " +
                "                   AND assess.application_id in (" + applicationIds.stream().map(e -> e.toString()).collect(Collectors.joining(",")) + ") " +
                "                   AND assess.status = 'COMPLETE' " +
                "            window w_risk_count as (partition by assess.id, so.risk) " +
                "    ) AS risks " +
                " GROUP BY ID, risk, application_id;";
            List<Tuple> query = entityManager.createNativeQuery(sql, Tuple.class).getResultList();

            List<AssessmentRiskDto> resultMappedToAssessmentsRisk = query.stream()
                .map(this::sqlRowToAssessmentRisk)
                .collect(Collectors.toList());

            List<LandscapeDto> collect = resultMappedToAssessmentsRisk.stream()
                .collect(Collectors.groupingBy(e -> e.id, Collectors.mapping(Function.identity(), Collectors.maxBy(this::compareAssessmentRisk))))
                .values().stream()
                .map(a -> new LandscapeDto(a.get().getId().longValue(), "UNKNOWN".equalsIgnoreCase(a.get().getRisk()) ? Risk.GREEN : Risk.valueOf(a.get().getRisk()), a.get().applicationId.longValue()))
                .collect(toList());
            return collect;
    }
    @Transactional
    public List<RiskLineDto> identifiedRisks(List<Long> applicationList, String language) {
        //String sqlString = "select cat.category_order, cat.name, q.question_order, q.question_text, opt.singleoption_order, opt.option, cast(array_agg(a.application_id) as text) \n" +
        String sqlString = " select c.id as cid, q.id as qid, so.id as soid, " +
                           " cat.category_order, que.question_order, opt.singleoption_order, \n" +
                           " cast(array_agg(a.application_id) as text) as applicationIds \n" +
                " from assessment_category cat join assessment_question que on cat.id = que.assessment_category_id \n" +
                "                             join assessment_singleoption opt on que.id = opt.assessment_question_id and opt.selected is true \n" +
                "                             join assessment_questionnaire aq on cat.assessment_questionnaire_id = aq.id \n" +
                "                             join assessment a on aq.assessment_id = a.id\n " +
                "                             join category c on (c.id = cat.questionnaire_categoryid)\n " +
                "                             join question q on (q.id = que.questionnaire_questionid) \n " +
                "                             join single_option so on (so.id = opt.questionnaire_optionid) \n" +
                " where cat.deleted is not true\n" +
                "      AND q.deleted is not true\n" +
                "      AND opt.deleted is not true\n" +
                "      AND aq.deleted is not true\n" +
                "      AND a.deleted is not true\n" +
                "      AND a.application_id in (" + StringUtils.join(applicationList, ",") + ") " +
                "      AND opt.risk = 'RED' " +
                " group by cid, qid, soid, cat.category_order, que.question_order, opt.singleoption_order \n" +
                " order by cat.category_order, que.question_order, opt.singleoption_order;";


        List<Tuple> query = entityManager.createNativeQuery(sqlString, Tuple.class).getResultList();
        return assessmentMapper.riskListQueryToRiskLineDtoList(query, language);
    }
    @Transactional
    public List<AdoptionCandidateDto> getAdoptionCandidate(List<Long> applicationId) {
        return applicationId.stream()
            .map(a-> Assessment.find("applicationId", a).firstResultOptional())
            .filter(b -> b.isPresent())
            .filter(b -> ((Assessment) b.get()).status == AssessmentStatus.COMPLETE)
            .map(c -> new AdoptionCandidateDto(((Assessment) c.get()).applicationId, ((Assessment) c.get()).id, calculateConfidence((Assessment) c.get())))
            .collect(Collectors.toList());
    }

    private Integer calculateConfidence(Assessment assessment) {
        Map<Risk, Integer> weightMap = Map.of(Risk.RED, redWeight,
                                            Risk.UNKNOWN, unknownWeight,
                                            Risk.AMBER, amberWeight,
                                            Risk.GREEN, greenWeight);

        List<AssessmentSingleOption> answeredOptions = assessment.assessmentQuestionnaire.categories.stream()
            .flatMap(cat -> cat.questions.stream())
            .flatMap(que -> que.singleOptions.stream())
            .filter(opt -> opt.selected)
            .collect(Collectors.toList());

        long totalQuestions = assessment.assessmentQuestionnaire.categories.stream().flatMap(cat -> cat.questions.stream()).count();

        // Grouping to know how many answers per Risk
        Map<Risk, Long> answersCountByRisk = answeredOptions.stream()
            .collect(Collectors.groupingBy(a -> a.risk, Collectors.counting()));


        BigDecimal result = getConfidenceOldPathfinder(weightMap, answeredOptions, totalQuestions, answersCountByRisk);

        return result.intValue();
    }

    private BigDecimal getConfidenceOldPathfinder(Map<Risk, Integer> weightMap, List<AssessmentSingleOption> answeredOptions, long totalQuestions, Map<Risk, Long> answersCountByRisk) {
        Map<Risk, Double> confidenceMultiplier = Map.of(Risk.RED, redMultiplier, Risk.AMBER, amberMultiplier);
        Map<Risk, Double> adjusterBase = Map.of(Risk.RED, redAdjuster, Risk.AMBER, amberAdjuster, Risk.GREEN, greenAdjuster, Risk.UNKNOWN, unknownAdjuster);

        // Adjuster calculation
        AtomicDouble adjuster = new AtomicDouble(1);
        answersCountByRisk.entrySet().stream()
            .filter(a -> a.getValue() > 0 )
            .forEach(b -> updateAdjuster(adjusterBase, adjuster, b));

        // Temp confidence iteration calculation
        // TODO Apparently this formula seems wrong, as the first execution in the forEach is multiplying by 0
        AtomicDouble confidence = new AtomicDouble(0.0);

        answeredOptions.stream()
            .sorted(Comparator.comparing(a -> weightMap.get(a.risk))) // sorting by weight to put REDs first
            .forEach(opt -> {
                confidence.set(confidence.get() * confidenceMultiplier.getOrDefault(opt.risk, 1.0));
                confidence.getAndAdd(weightMap.get(opt.risk) * adjuster.get());
            });

        double maxConfidence = weightMap.get(Risk.GREEN) * totalQuestions;

        BigDecimal result = (maxConfidence > 0 ) ? new BigDecimal((confidence.get() / maxConfidence) * 100) : BigDecimal.ZERO;
        result.setScale(0, RoundingMode.DOWN);
        return result;
    }

    private void updateAdjuster(Map<Risk, Double> adjusterBase, AtomicDouble adjuster, Map.Entry<Risk, Long> b) {
        adjuster.set(adjuster.get() * Math.pow(adjusterBase.get(b.getKey()), b.getValue()));
    }

    private AssessmentRiskDto sqlRowToAssessmentRisk(Tuple row) {
        return new AssessmentRiskDto(row.get("id", BigInteger.class).longValue(), row.get("risk", String.class), row.get("percentage", Integer.class), row.get("application_id", BigInteger.class).longValue());
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
        Long id;
        String risk;
        Integer percentage;
        Long applicationId;
    }
}
