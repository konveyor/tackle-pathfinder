package io.tackle.pathfinder.services;

import com.google.common.util.concurrent.AtomicDouble;
import io.quarkus.arc.config.ConfigProperties;
import io.tackle.pathfinder.dto.AdoptionCandidateDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentStatus;
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
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Log
public class AssessmentSvc {
    @Inject
    AssessmentMapper mapper;

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

    public Optional<AssessmentHeaderDto> getAssessmentHeaderDtoByApplicationId(@NotNull Long applicationId) {
        List<Assessment> assessmentQuery = Assessment.list("application_id", applicationId);
        return assessmentQuery.stream().findFirst().map(e -> mapper.assessmentToAssessmentHeaderDto(e));
    }

    @Transactional
    public AssessmentHeaderDto createAssessment(@NotNull Long applicationId) {
        long count = Assessment.count("application_id", applicationId);
        log.log(Level.FINE,"Assessments for application_id [ " + applicationId + "] : " + count);
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
                if (assessmentDto.getStakeholderGroups().stream().noneMatch(f -> f.equals(stakegroup.stakeholdergroupId))) {
                    log.log(Level.FINE,"Deleted stakegroup : " + stakegroup.stakeholdergroupId);
                    stakegroup.delete();
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

    @Transactional
    public List<AdoptionCandidateDto> getAdoptionCandidate(List<Long> applicationId) {
        return applicationId.stream()
            .map(a-> Assessment.find("applicationId", a).firstResultOptional())
            .filter(b -> b.isPresent())
            .map(c -> new AdoptionCandidateDto(((Assessment) c.get()).applicationId, ((Assessment) c.get()).id, calculateConfidence((Assessment) c.get())))
            .collect(Collectors.toList());
    }

    private Integer calculateConfidence(Assessment assessment) {
        Map<Risk, Integer> weightMap = Map.of(Risk.RED, redWeight,
                                            Risk.UNKNOWN, unknownWeight,
                                            Risk.AMBER, amberWeight,
                                            Risk.GREEN, greenWeight);

        Stream<AssessmentSingleOption> answeredOptions = assessment.assessmentQuestionnaire.categories.stream()
            .flatMap(cat -> cat.questions.stream())
            .flatMap(que -> que.singleOptions.stream())
            .filter(opt -> opt.selected);
        long totalAnswered = answeredOptions.count();

        // Grouping to know how many answers per Risk
        Map<Risk, Long> answersCountByRisk = answeredOptions
            .collect(Collectors.groupingBy(a -> a.risk, Collectors.counting()));


        BigDecimal result = getConfidenceOldPathfinder(weightMap, answeredOptions, totalAnswered, answersCountByRisk);

        return result.intValue();
    }

    private BigDecimal getConfidenceOldPathfinder(Map<Risk, Integer> weightMap, Stream<AssessmentSingleOption> answeredOptions, long totalAnswered, Map<Risk, Long> answersCountByRisk) {
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

        answeredOptions
            .sorted(Comparator.comparing(a -> weightMap.get(a.risk))) // sorting by weight to put REDs first
            .forEach(opt -> {
                confidence.set(confidence.get() * confidenceMultiplier.getOrDefault(opt.risk, 1.0));
                confidence.getAndAdd(weightMap.get(opt.risk) * adjuster.get());
            });

        double maxConfidence = weightMap.get(Risk.GREEN) * totalAnswered;

        BigDecimal result = new BigDecimal((confidence.get() / maxConfidence) * 100);
        result.setScale(0, RoundingMode.DOWN);
        return result;
    }

    private BigDecimal getConfidenceTacklePathfinder(Map<Risk, Integer> weightMap, Stream<AssessmentSingleOption> answeredOptions, long totalAnswered, Map<Risk, Long> answersCountByRisk) {
        Map<Risk, Double> adjusterBase = Map.of(Risk.RED, redAdjuster, Risk.AMBER, amberAdjuster, Risk.GREEN, greenAdjuster, Risk.UNKNOWN, unknownAdjuster);

        double answeredWeight = answeredOptions.mapToDouble(a -> weightMap.get(a.risk) * adjusterBase.getOrDefault(a.risk, 1d)).sum();

        long maxWeight = weightMap.get(Risk.GREEN) * totalAnswered;

        BigDecimal result = new BigDecimal(answeredWeight / maxWeight * 100);
        result.setScale(0, RoundingMode.DOWN);
        return result;
    }

    //         if (redCount > 0) adjuster = adjuster * Math.pow(0.5, redCount);
    //        if (amberCount > 0) adjuster = adjuster * Math.pow(0.98, amberCount);
    private void updateAdjuster(Map<Risk, Double> adjusterBase, AtomicDouble adjuster, Map.Entry<Risk, Long> b) {
        adjuster.set(adjuster.get() * Math.pow(adjusterBase.get(b.getKey()), b.getValue()));
    }
}
