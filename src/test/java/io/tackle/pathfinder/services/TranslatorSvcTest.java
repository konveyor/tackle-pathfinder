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

import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        translatorSvc.addTranslation(category, "name" , "Category Traducido al castellano", "ES");
        translatorSvc.addTranslation(category, "name" , "Category Traduit al catala", "CAT");

        AssessmentQuestion assessmentQuestion = assessmentCategory.questions
            .stream()
            .filter(e -> e.order == 1)
            .findFirst()
            .get();
        Question question = Question.findById(assessmentQuestion.questionnaire_questionId);
        translatorSvc.addTranslation(question, "question" , "Question Traducido al castellano", "ES");
        translatorSvc.addTranslation(question, "question" , "Question Traduit al catala", "CAT");
        translatorSvc.addTranslation(question, "description" , "Question Description Traduit al catala", "CAT");

        AssessmentSingleOption assessmentOption = assessmentQuestion.singleOptions
            .stream()
            .filter(e -> e.order == 1)
            .findFirst()
            .get();
        SingleOption singleOption = SingleOption.findById(assessmentOption.questionnaire_optionId);
        translatorSvc.addTranslation(singleOption, "option" , "Option Traducido al castellano", "ES");
        translatorSvc.addTranslation(singleOption, "option" , "Option Traduit al catala", "CAT");

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

}