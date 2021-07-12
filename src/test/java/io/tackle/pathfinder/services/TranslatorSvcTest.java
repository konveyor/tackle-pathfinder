package io.tackle.pathfinder.services;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentQuestion;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        AssessmentDto assessmentDtoES = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "ES");
        AssessmentDto assessmentDtoCAT = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "CAT");

        // then
        assertThat(assessmentDtoEN.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).doesNotStartWith("ES:").doesNotStartWith("IT:").isNotBlank();

        // Checking 2 existing languages have translated texts
        assertThat(assessmentDtoES.getQuestionnaire().getLanguage()).isEqualTo("ES");
        assertThat(assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).startsWith("ES:");
        AssessmentQuestionDto assessmentQuestionDtoES = assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getQuestions().stream()
            .filter(a -> a.getOrder() == 1)
            .findFirst().get();
        assertThat(assessmentQuestionDtoES.getQuestion()).startsWith("ES:");
        assertThat(assessmentQuestionDtoES.getDescription()).startsWith("ES:");

        AssessmentQuestionOptionDto assessmentOptionDtoES = assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getQuestions().stream()
            .filter(a -> a.getOrder() == 1)
            .findFirst().get().getOptions().stream()
            .filter(b -> b.getOrder() == 1)
            .findFirst().get();
        assertThat(assessmentOptionDtoES.getOption()).startsWith("ES:");

        // Checking a language not existing, should give the original texts
        assertThat(assessmentDtoCAT.getQuestionnaire().getLanguage()).isEqualTo("CAT");
        assertThat(assessmentDtoCAT.getQuestionnaire().getCategories()
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

        AssessmentDto assessmentDtoES = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "ES");
        assertThat(assessmentDtoES.getQuestionnaire().getLanguage()).isEqualTo("ES");
        assertThat(assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).doesNotStartWith("ES:").isNotBlank(); // no translation happened
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

        AssessmentDto assessmentDtoES = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "ES");
        assertThat(assessmentDtoES.getQuestionnaire().getLanguage()).isEqualTo("ES");
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
        translatorSvc.addOrUpdateTranslation(category, "name", "CAT: Category Traduit al catala", "CAT");

        // when
        AssessmentDto assessmentDtoCAT = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "CAT");

        // then
        assertThat(assessmentDtoCAT.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).startsWith("CAT:");

        // when
        translatorSvc.addOrUpdateTranslation(category, "name", "CAT: Category Traduit al catala", "CAT").delete();
        AssessmentDto assessmentDtoCAT2 = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "CAT");
        // then
        assertThat(assessmentDtoCAT2.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).doesNotStartWith("CAT:"); // No translation made
    }

}