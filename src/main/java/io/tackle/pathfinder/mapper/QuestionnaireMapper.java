/*
 * Copyright © 2021 Konveyor (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
