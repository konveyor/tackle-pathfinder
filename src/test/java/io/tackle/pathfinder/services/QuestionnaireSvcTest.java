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

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.tackle.pathfinder.DefaultTestProfile;
import io.tackle.pathfinder.dto.questionnaire.QuestionnaireHeaderDto;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestProfile(DefaultTestProfile.class)
class QuestionnaireSvcTest {
    @Inject
    QuestionnaireSvc questionnaireSvc;

    @Inject
    TranslatorSvc translatorSvc;

    @Test
    public void given_FewQuestionnaires_when_GetQuestionnaires_then_ListOfQuestionnaireHeaderIsReturned() {
        List<QuestionnaireHeaderDto> questionnaireHeaderList = questionnaireSvc.getQuestionnaireHeaderList(null);
        assertThat(questionnaireHeaderList).size().isGreaterThan(0);
        assertThat(questionnaireHeaderList)
            .extracting("name", "language")
            .contains(Tuple.tuple("Pathfinder", "EN"));
    }

    @Test
    public void given_FewQuestionnaires_when_GetQuestionnairesWithALanguage_then_ListOfQuestionnaireHeaderIsReturnedInThatLanguage() {
        translatorSvc.addOrUpdateTranslation(Questionnaire.findAll().firstResult(), "name", "CAT Pathfinder", "CA");
        List<QuestionnaireHeaderDto> questionnaireHeaderList = questionnaireSvc.getQuestionnaireHeaderList("CA");
        assertThat(questionnaireHeaderList).size().isGreaterThan(0);
        assertThat(questionnaireHeaderList)
            .extracting("name", "language")
            .contains(Tuple.tuple("CAT Pathfinder", "CA"));
    }
}
