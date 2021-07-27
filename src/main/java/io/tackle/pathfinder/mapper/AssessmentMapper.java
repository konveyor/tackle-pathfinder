package io.tackle.pathfinder.mapper;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.model.assessment.*;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import io.tackle.pathfinder.services.TranslatorSvc;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import javax.inject.Inject;
import javax.transaction.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public abstract class AssessmentMapper extends TranslatorMapper{

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
    @Mapping(target = "language", expression="java(org.apache.commons.lang3.StringUtils.defaultString(language, questionnaire.languageCode))")
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

    private RiskLineDto getRiskLineDto(Object[] fields, @Context String language) {
        // c.id, q.id, so.id, c.category_order, q.question_order, opt.singleoption_order, cast(array_agg(a.application_id) as text
        String fieldApps = (String) fields[6];
        String[] appsList = fieldApps.replace("{", "").replace("}", "").split(",");

        BigInteger categoryId = (BigInteger) fields[0];
        BigInteger questionId = (BigInteger) fields[1];
        BigInteger optionId = (BigInteger) fields[2];

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

    public List<RiskLineDto> riskListQueryToRiskLineDtoList(List<Object[]> objectList, @Context String language) {
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
}
