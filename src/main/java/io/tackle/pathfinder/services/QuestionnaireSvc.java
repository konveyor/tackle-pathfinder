/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
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
package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.questionnaire.QuestionnaireHeaderDto;
import io.tackle.pathfinder.mapper.QuestionnaireMapper;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class QuestionnaireSvc {
    @Inject
    QuestionnaireMapper questionnaireMapper;

    public List<QuestionnaireHeaderDto> getQuestionnaireHeaderList(String lang) {
        return Questionnaire.listAll().stream()
            .map(a -> questionnaireMapper.questionnaireToQuestionnaireHeaderDto((Questionnaire) a, lang))
            .collect(Collectors.toList());
    }
}
