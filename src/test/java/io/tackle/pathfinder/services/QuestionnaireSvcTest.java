package io.tackle.pathfinder.services;

import io.quarkus.test.junit.QuarkusTest;
import io.tackle.pathfinder.dto.questionnaire.QuestionnaireHeaderDto;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
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