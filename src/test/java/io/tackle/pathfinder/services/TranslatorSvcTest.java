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

        AssessmentCategory assessmentCategory = ((Assessment) Assessment.findById(assessmentDtoEN.getId())).assessmentQuestionnaire.categories
            .stream()
            .filter(e -> e.order == 1)
            .findFirst()
            .get();
        Category category = Category.findById(assessmentCategory.questionnaire_categoryId);
        translatorSvc.addOrUpdateTranslation(category, "name" , "Category Traducido al castellano", "ES");
        translatorSvc.addOrUpdateTranslation(category, "name" , "Category Traduit al catala", "CAT");

        AssessmentQuestion assessmentQuestion = assessmentCategory.questions
            .stream()
            .filter(e -> e.order == 1)
            .findFirst()
            .get();
        Question question = Question.findById(assessmentQuestion.questionnaire_questionId);
        translatorSvc.addOrUpdateTranslation(question, "question" , "Question Traducido al castellano", "ES");
        translatorSvc.addOrUpdateTranslation(question, "question" , "Question Traduit al catala", "CAT");
        translatorSvc.addOrUpdateTranslation(question, "description" , "Question Description Traduit al catala", "CAT");

        AssessmentSingleOption assessmentOption = assessmentQuestion.singleOptions
            .stream()
            .filter(e -> e.order == 1)
            .findFirst()
            .get();
        SingleOption singleOption = SingleOption.findById(assessmentOption.questionnaire_optionId);
        translatorSvc.addOrUpdateTranslation(singleOption, "option" , "Option Traducido al castellano", "ES");
        translatorSvc.addOrUpdateTranslation(singleOption, "option" , "Option Traduit al catala", "CAT");

        // when
        AssessmentDto assessmentDtoES = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "ES");
        AssessmentDto assessmentDtoCAT = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "CAT");
        AssessmentDto assessmentDtoIT = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "IT");

        // then
        assertThat(assessmentDtoEN.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isEqualTo(assessmentCategory.name);

        // Checking 2 existing languages have translated texts
        assertThat(assessmentDtoES.getQuestionnaire().getLanguage()).isEqualTo("ES");
        assertThat(assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isEqualTo("Category Traducido al castellano");
        AssessmentQuestionDto assessmentQuestionDtoES = assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getQuestions().stream()
            .filter(a -> a.getOrder() == 1)
            .findFirst().get();
        assertThat(assessmentQuestionDtoES.getQuestion()).isEqualTo("Question Traducido al castellano");
        assertThat(assessmentQuestionDtoES.getDescription()).isEqualTo(question.description);

        AssessmentQuestionOptionDto assessmentOptionDtoES = assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getQuestions().stream()
            .filter(a -> a.getOrder() == 1)
            .findFirst().get().getOptions().stream()
            .filter(b -> b.getOrder() == 1)
            .findFirst().get();
        assertThat(assessmentOptionDtoES.getOption()).isEqualTo("Option Traducido al castellano");

        assertThat(assessmentDtoCAT.getQuestionnaire().getLanguage()).isEqualTo("CAT");
        assertThat(assessmentDtoCAT.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isEqualTo("Category Traduit al catala");
        AssessmentQuestionDto assessmentQuestionDtoCAT = assessmentDtoCAT.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getQuestions().stream()
            .filter(a -> a.getOrder() == 1)
            .findFirst().get();
        assertThat(assessmentQuestionDtoCAT.getQuestion()).isEqualTo("Question Traduit al catala");
        assertThat(assessmentQuestionDtoCAT.getDescription()).isEqualTo("Question Description Traduit al catala");

        AssessmentQuestionOptionDto assessmentOptionDtoCAT = assessmentDtoCAT.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getQuestions().stream()
            .filter(a -> a.getOrder() == 1)
            .findFirst().get().getOptions().stream()
            .filter(b -> b.getOrder() == 1)
            .findFirst().get();
        assertThat(assessmentOptionDtoCAT.getOption()).isEqualTo("Option Traduit al catala");

        // Checking a language not existing, should give the original texts
        assertThat(assessmentDtoIT.getQuestionnaire().getLanguage()).isEqualTo("IT");
        assertThat(assessmentDtoIT.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isEqualTo(assessmentCategory.name);
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

        // we translate ALL categories to Spanish and Catalan
        Category.streamAll().forEach(cat -> {
            translatorSvc.addOrUpdateTranslation((PanacheEntity) cat, "name" , "Category Traducido al castellano", "ES");
            translatorSvc.addOrUpdateTranslation((PanacheEntity) cat, "name" , "Category Traduit al catala", "CAT");
        });

        AssessmentDto assessmentDtoES = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "ES");
        assertThat(assessmentDtoES.getQuestionnaire().getLanguage()).isEqualTo("ES");
        assertThat(assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isNotEqualTo("Category Traducido al castellano"); // no translation happened
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

        // we translate ALL categories to Spanish and Catalan
        Category.streamAll().forEach(cat -> {
            translatorSvc.addOrUpdateTranslation((PanacheEntity) cat, "name" , "Category Traducido al castellano", "ES");
            translatorSvc.addOrUpdateTranslation((PanacheEntity) cat, "name" , "Category Traduit al catala", "CAT");
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
        translatorSvc.addOrUpdateTranslation(category, "name", "Category Traducido al castellano", "ES");
        translatorSvc.addOrUpdateTranslation(category, "name", "Category Traduit al catala", "CAT").delete();

        // when
        AssessmentDto assessmentDtoES = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "ES");
        AssessmentDto assessmentDtoCAT = assessmentSvc.getAssessmentDtoByAssessmentId(assessmentDtoEN.getId(), "CAT");

        // then
        assertThat(assessmentDtoES.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isEqualTo("Category Traducido al castellano");

        assertThat(assessmentDtoCAT.getQuestionnaire().getCategories()
            .stream()
            .filter(e -> e.getOrder() == 1)
            .findFirst()
            .get().getTitle()).isEqualTo(assessmentCategory.name); // No translation made
    }

}