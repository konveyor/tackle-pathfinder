package io.tackle.pathfinder.mapper;

import io.tackle.pathfinder.dto.questionnaire.QuestionnaireHeaderDto;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public abstract class QuestionnaireMapper extends TranslatorMapper {
    @Mapping(target = "language", expression="java(org.apache.commons.lang3.StringUtils.defaultString(language, questionnaire.languageCode))")
    @Mapping(target = "name", expression="java(translate(\"Questionnaire\", questionnaire.id, questionnaire.name, language, \"name\"))")
    public abstract QuestionnaireHeaderDto questionnaireToQuestionnaireHeaderDto(Questionnaire questionnaire, @Context String language);
}
