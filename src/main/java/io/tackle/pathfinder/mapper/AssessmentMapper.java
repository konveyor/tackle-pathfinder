package io.tackle.pathfinder.mapper;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.model.assessment.*;
import io.tackle.pathfinder.model.bulk.AssessmentBulk;
import io.tackle.pathfinder.model.bulk.AssessmentBulkApplication;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public abstract class AssessmentMapper extends TranslatorMapper {

    public abstract AssessmentHeaderDto assessmentToAssessmentHeaderDto(Assessment assessment);

    @Mapping(target="checked", source="selected")
    @Mapping(target = "option", expression="java(translateOption(option, option.option, language, \"option\"))")
    public abstract AssessmentQuestionOptionDto assessmentSingleOptionToAssessmentQuestionOptionDto(AssessmentSingleOption option, @Context String language);
    public abstract List<AssessmentQuestionOptionDto> assessmentSingleOptionListToassessmentQuestionOptionDtoList(List<AssessmentSingleOption> optionList, @Context String language);

    @Mapping(target = "options", source="singleOptions")
    @Mapping(target = "question", expression="java(translateQuestion(question, question.questionText, language, \"question\"))")
    @Mapping(target = "description", expression="java(translateQuestion(question, question.description, language, \"description\"))")
    public abstract AssessmentQuestionDto assessmentQuestionToAssessmentQuestionDto(AssessmentQuestion question, @Context String language);
    public abstract List<AssessmentQuestionDto> assessmentQuestionListToassessmentQuestionDtoList(List<AssessmentQuestion> questionList, @Context String language);

    @Mapping(target="title", expression="java(translateCategory(category, category.name, language, \"name\"))")
    public abstract AssessmentCategoryDto assessmentCategoryToAssessmentCategoryDto(AssessmentCategory category, @Context String language);

    public abstract List<AssessmentCategoryDto> assessmentCategoryListToAssessmentCategoryDtoList(List<AssessmentCategory> categoryList, @Context String language);

    @Mapping(target="title", source="name")
    @Mapping(target = "language", expression="java(language)")
    public abstract AssessmentQuestionnaireDto assessmentQuestionnaireToAssessmentQuestionnaireDto(AssessmentQuestionnaire questionnaire, @Context String language);

    public List<Long> assessmentStakeholderListToLongList(List<AssessmentStakeholder> stakeholder) {
        return stakeholder.stream().map(e -> e.stakeholderId).collect(Collectors.toList());
    }
    public List<Long> assessmentStakeholderGroupListToLongList(List<AssessmentStakeholdergroup> stakeholdergroup) {
        return stakeholdergroup.stream().map(e -> e.stakeholdergroupId).collect(Collectors.toList());
    }

    @Mapping(target = "questionnaire", source = "assessmentQuestionnaire")
    @Mapping(target = "stakeholderGroups", source = "stakeholdergroups")
    public abstract AssessmentDto assessmentToAssessmentDto(Assessment assessment, @Context String language);

    private RiskLineDto getRiskLineDto(Tuple fields, @Context String language) {
        // cat.category_order, cat.name, q.question_order, q.question_text, opt.singleoption_order, opt.option, array_agg(a.application_id)
        String fieldApps = fields.get("applicationIds", String.class);
        String[] appsList = fieldApps.replace("{", "").replace("}", "").split(",");

        BigInteger categoryId = fields.get("cid", BigInteger.class);
        BigInteger questionId = fields.get("qid", BigInteger.class);
        BigInteger optionId = fields.get("soid", BigInteger.class);

        Category category = Category.findById(categoryId.longValue());
        Question question = Question.findById(questionId.longValue());
        SingleOption option = SingleOption.findById(optionId.longValue());

        String categoryText = translate(category, category.name, language, "name");
        String questionText = translate(question, question.questionText, language, "question");
        String optionText = translate(option, option.option, language, "option");
        List<Long> applications = Arrays.stream(appsList).map(Long::parseLong).collect(Collectors.toList());

        return RiskLineDto.builder()
            .category(categoryText)
            .question(questionText)
            .answer(optionText)
            .applications(applications)
            .build();
    }

    public List<RiskLineDto> riskListQueryToRiskLineDtoList(List<Tuple> objectList, @Context String language) {
        return objectList.stream().map(a -> getRiskLineDto(a, language)).collect(Collectors.toList());
    }

    @Transactional
    protected String translateCategory(AssessmentCategory cat, String defaultText, String destLanguage, String field) {
        return Optional.ofNullable(cat.questionnaire_categoryId)
                .flatMap(a -> Category.findByIdOptional(a))
                .map(a -> translate((PanacheEntity) a, defaultText, destLanguage, field))
            .orElse(defaultText);
    }

    @Transactional
    protected String translateQuestion(AssessmentQuestion que, String defaultText, String destLanguage, String field) {
        return Optional.ofNullable(que.questionnaire_questionId)
            .flatMap(a -> Question.findByIdOptional(a))
            .map(a -> translate((PanacheEntity) a, defaultText, destLanguage, field))
            .orElse(defaultText);
    }

    @Transactional
    protected String translateOption(AssessmentSingleOption opt, String defaultText, String destLanguage, String field) {
        return Optional.ofNullable(opt.questionnaire_optionId)
            .flatMap(a -> SingleOption.findByIdOptional(a))
            .map(a -> translate((PanacheEntity) a, defaultText, destLanguage, field))
            .orElse(defaultText);
    }

    public AssessmentBulkDto assessmentBulkToAssessmentBulkDto(AssessmentBulk bulk) {
        return AssessmentBulkDto.builder()
            .bulkId(bulk.id)
            .applications(bulk.bulkApplications.stream().map(f -> f.applicationId).collect(Collectors.toList()))
            .completed(bulk.completed)
            .fromAssessmentId(bulk.fromAssessmentId)
            .assessments(bulk.bulkApplications.stream().map(this::assessmentBulkApplicationToAssessmentHeaderBulkDto).collect(Collectors.toList()))
            .build();
    }

    protected AssessmentHeaderBulkDto assessmentBulkApplicationToAssessmentHeaderBulkDto(AssessmentBulkApplication application) {
        AssessmentHeaderBulkDto dto = AssessmentHeaderBulkDto.builder()
            .applicationId(application.applicationId)
            .id(application.assessmentId)
            .error(application.error)
            .build();
        if (application.assessmentId != null) {
            Assessment assessment = Assessment.findById(application.assessmentId);
            dto.setStatus(assessment.status);
        }
        return dto;
    }
}
