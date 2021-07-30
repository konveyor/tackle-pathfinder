package io.tackle.pathfinder.mapper;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.tackle.pathfinder.dto.questionnaire.*;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public abstract class QuestionnaireMapper extends TranslatorMapper {
    @Mapping(target = "language", expression="java(org.apache.commons.lang3.StringUtils.defaultString(language, questionnaire.languageCode))")
    @Mapping(target = "name", expression="java(translate(\"Questionnaire\", questionnaire.id, questionnaire.name, language, \"name\"))")
    public abstract QuestionnaireHeaderDto questionnaireToQuestionnaireHeaderDto(Questionnaire questionnaire, @Context String language);

    @Mapping(target = "language", expression="java(org.apache.commons.lang3.StringUtils.defaultString(language, questionnaire.languageCode))")
    @Mapping(target = "name", expression="java(translate(\"Questionnaire\", questionnaire.id, questionnaire.name, language, \"name\"))")
    public abstract QuestionnaireDto questionnaireToQuestionnaireDto(Questionnaire questionnaire, @Context String language);

    @Mapping(target = "title", expression="java(translate(\"Category\", category.id, category.name, language, \"name\"))")
    public abstract CategoryDto categoryToCategoryDto(Category category, @Context String language);

    @Mapping(target = "description", expression="java(translate(\"Question\", question.id, question.description, language, \"description\"))")
    @Mapping(target = "question", expression="java(translate(\"Question\", question.id, question.questionText, language, \"text\"))")
    @Mapping(target = "options", source = "singleOptions")
    public abstract QuestionDto questionToQuestionDto(Question question, @Context String language);

    @Mapping(target = "option", expression="java(translate(\"SingleOption\", option.id, option.option, language, \"option\"))")
    public abstract QuestionOptionDto singleOptionToQuestionOptionDto(SingleOption option, @Context String language);
}
