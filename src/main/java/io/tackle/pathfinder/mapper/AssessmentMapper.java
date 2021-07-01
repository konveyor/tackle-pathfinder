package io.tackle.pathfinder.mapper;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.model.assessment.*;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import io.tackle.pathfinder.services.TranslatorSvc;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public abstract class AssessmentMapper {
    @Inject
    TranslatorSvc translatorSvc;

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

    private RiskLineDto getRiskLineDto(Object[] fields) {
        // cat.category_order, cat.name, q.question_order, q.question_text, opt.singleoption_order, opt.option, array_agg(a.application_id)
        String fieldApps = (String) fields[6];
        String[] appsList = fieldApps.replace("{", "").replace("}", "").split(",");
        List<Long> applications = Arrays.stream(appsList).map(Long::parseLong).collect(Collectors.toList());
        return RiskLineDto.builder()
            .category((String) fields[1])
            .question((String) fields[3])
            .answer((String) fields[5])
            .applications(applications)
            .build();
    }

    public List<RiskLineDto> riskListQueryToRiskLineDtoList(List<Object[]> objectList) {
        return objectList.stream().map(this::getRiskLineDto).collect(Collectors.toList());
    }

    protected String translateCategory(AssessmentCategory cat, String defaultText, String destLanguage, String field) {
        return translate(Category.findById(cat.questionnaire_categoryId), defaultText, destLanguage, field);
    }

    protected String translateQuestion(AssessmentQuestion que, String defaultText, String destLanguage, String field) {
        return translate(Question.findById(que.questionnaire_questionId), defaultText, destLanguage, field);
    }

    protected String translateOption(AssessmentSingleOption opt, String defaultText, String destLanguage, String field) {
        return translate(SingleOption.findById(opt.questionnaire_optionId), defaultText, destLanguage, field);
    }

    protected String translate(PanacheEntity dto, String defaultText, String destLanguage, String field) {
        // {table}_{id}_field  , dest language
        String key = translatorSvc.getKey(dto, field);
        return translatorSvc.translate(key, destLanguage, defaultText);
    }

}
