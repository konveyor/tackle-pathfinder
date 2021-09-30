package io.tackle.pathfinder.services;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentQuestionDto;
import io.tackle.pathfinder.dto.AssessmentQuestionOptionDto;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.i18n.TranslatedText;
import io.tackle.pathfinder.model.questionnaire.Category;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@QuarkusTestResource(value = PostgreSQLDatabaseTestResource.class,
    initArgs = {
        @ResourceArg(name = PostgreSQLDatabaseTestResource.DB_NAME, value = "pathfinder_db"),
        @ResourceArg(name = PostgreSQLDatabaseTestResource.USER, value = "pathfinder"),
        @ResourceArg(name = PostgreSQLDatabaseTestResource.PASSWORD, value = "pathfinder")
    }
)
@QuarkusTest
public class TranslatorSvcTest {
    @Inject
    TranslatorSvc translatorSvc;

    @Inject
    AssessmentSvc assessmentSvc;

    @Test
    @Transactional
    public void given_OneAssesment_when_RequestedInDifferentLanguages_then_TranslatedAndDefaultOriginalValuesAreReturned() {
        // given
        AssessmentHeaderDto assessmentDtoENHeader = assessmentSvc.createAssessment(2000L);
        AssessmentDto assessmentDtoEN = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoENHeader.getId(), "EN");

        // when
        AssessmentDto assessmentDtoCA = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "CA");
        AssessmentDto assessmentDtoFR = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "FR");

        // then
        assertThat(assessmentDtoEN.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).doesNotStartWith("CA:").doesNotStartWith("IT:").isNotBlank();

        // Checking 2 existing languages have translated texts
        assertThat(assessmentDtoCA.getQuestionnaire().getLanguage()).isEqualTo("CA");
        assertThat(assessmentDtoCA.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).startsWith("CA:");
        AssessmentQuestionDto assessmentQuestionDtoES = assessmentDtoCA.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getQuestions().stream()
            .filter(a -> a.getOrder() == 1)
            .findFirst().get();
        assertThat(assessmentQuestionDtoES.getQuestion()).startsWith("CA:");
        assertThat(assessmentQuestionDtoES.getDescription()).startsWith("CA:");

        AssessmentQuestionOptionDto assessmentOptionDtoES = assessmentDtoCA.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getQuestions().stream()
            .filter(a -> a.getOrder() == 1)
            .findFirst().get().getOptions().stream()
            .filter(b -> b.getOrder() == 1)
            .findFirst().get();
        assertThat(assessmentOptionDtoES.getOption()).startsWith("CA:");

        // Checking a language not existing, should give the original texts
        assertThat(assessmentDtoFR.getQuestionnaire().getLanguage()).isEqualTo("FR");
        assertThat(assessmentDtoFR.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isEqualTo(assessmentDtoEN.getQuestionnaire().getCategories()
                                        .stream()
                                        .filter(e -> e.getOrder() == 1)
                                        .findFirst()
                                        .get().getTitle());
    }

    @Test
    @Transactional
    public void given_OldAssessmentWithoutQuestionnaireIds_when_RequestedInAnyLanguage_then_OriginalTextsAndNoExceptionAreReturned() {
        // given
        AssessmentHeaderDto assessmentDtoENHeader = assessmentSvc.createAssessment(3000L);
        AssessmentDto assessmentDtoEN = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoENHeader.getId(), "EN");

        Assessment assessment = Assessment.findById(assessmentDtoENHeader.getId());

        // we reset all questionnaire element links, to simulate old assessments
        assessment.assessmentQuestionnaire.categories.stream()
            .forEach(a -> {
                a.questionnaire_categoryId = null;
                a.questions.stream()
                    .forEach(b -> {
                        b.questionnaire_questionId = null;
                        b.singleOptions.stream().forEach(c -> c.questionnaire_optionId = null);
                    });
            });

        AssessmentDto assessmentDtoES = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "CA");
        assertThat(assessmentDtoES.getQuestionnaire().getLanguage()).isEqualTo("CA");
        assertThat(assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).doesNotStartWith("CA:").isNotBlank(); // no translation happened
    }

    @Test
    @Transactional
    public void given_OldAssessmentWithUnexistingQuestionnaireIds_when_RequestedInAnyLanguage_then_OriginalTextsAndNoExceptionAreReturned() {
        // given
        AssessmentHeaderDto assessmentDtoENHeader = assessmentSvc.createAssessment(4000L);
        AssessmentDto assessmentDtoEN = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoENHeader.getId(), "EN");

        Assessment assessment = Assessment.findById(assessmentDtoENHeader.getId());
        String categoryOrder1 = assessment.assessmentQuestionnaire.categories
            .stream()
            .filter(e -> e.order == 1)
            .findFirst()
            .get().name;

        // we set all questionnaire element links with unexisting Ids
        assessment.assessmentQuestionnaire.categories.stream()
            .forEach(a -> {
                a.questionnaire_categoryId = 99999L;
                a.questions.stream()
                    .forEach(b -> {
                        b.questionnaire_questionId = 99999L;
                        b.singleOptions.stream().forEach(c -> c.questionnaire_optionId = 99999L);
                    });
            });

        AssessmentDto assessmentDtoES = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "CA");
        assertThat(assessmentDtoES.getQuestionnaire().getLanguage()).isEqualTo("CA");
        assertThat(assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isEqualTo(categoryOrder1); // no translation happened
    }
    @Test
    @Transactional
    public void given_OneAssesmentWithDeletedTranslations_when_RequestedInDifferentLanguages_then_OriginalValuesAreReturned() {
        // given
        AssessmentHeaderDto assessmentDtoENHeader = assessmentSvc.createAssessment(5000L);
        AssessmentDto assessmentDtoEN = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoENHeader.getId(), "EN");

        AssessmentCategory assessmentCategory = ((Assessment) Assessment.findById(assessmentDtoEN.getId())).assessmentQuestionnaire.categories
            .stream()
            .filter(e -> e.order == 1)
            .findFirst()
            .get();
        Category category = Category.findById(assessmentCategory.questionnaire_categoryId);
        translatorSvc.addOrUpdateTranslation(category, "name", "JP: Category Traduit al japanese", "JP");

        // when
        AssessmentDto assessmentDtoCA = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "JP");

        // then
        assertThat(assessmentDtoCA.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).startsWith("JP:");

        // when
        translatorSvc.addOrUpdateTranslation(category, "name", "JP: Category Traduit al japanese", "JP").delete();
        AssessmentDto assessmentDtoCA2 = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "JP");
        // then
        assertThat(assessmentDtoCA2.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).doesNotStartWith("JP:"); // No translation made
    }

    @Test
    @Transactional
    public void given_AllTheTranslationsAddedForSpanish_when_CheckingAllTranslationsInTable_then_correctNumberIsReturned() {
        List<PanacheEntityBase> listTranslations = TranslatedText.list("language", "ES");
        assertThat(listTranslations).size().isEqualTo(235);
    }

}
