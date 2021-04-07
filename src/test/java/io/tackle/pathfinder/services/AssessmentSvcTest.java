package io.tackle.pathfinder.services;

import io.quarkus.test.junit.QuarkusTest;
import io.tackle.pathfinder.model.Risk;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Log
public class AssessmentSvcTest {
    @Inject
    AssessmentSvc assessmentSvc;

    @Test
    @Transactional
    public void given_Questionnaire_when_CopyQuestionnaireIntoAssessment_should_BeIdentical() {
        Questionnaire questionnaire = createQuestionnaire();
        Assessment assessment = createAssessment(questionnaire.id);

        // same categories
        log.info("Assess Categories : " + assessment.assessmentQuestionnaire.categories.size());
        log.info("Categories : " + questionnaire.categories.size());
        assertThat(assessment.assessmentQuestionnaire.categories.size()).isEqualTo(questionnaire.categories.size());

        // same questions
        assertThat(assessment.assessmentQuestionnaire.categories.stream()
                .collect(Collectors.summarizingInt(p -> p.questions.size())).getSum())
                        .isEqualTo(questionnaire.categories.stream()
                                .collect(Collectors.summarizingInt(p -> p.questions.size())).getSum());

        // same options
        assertThat(assessment.assessmentQuestionnaire.categories.stream()
                .mapToInt(e -> e.questions.stream().mapToInt(f -> f.singleOptions.size()).sum()).sum())
                        .isEqualTo(questionnaire.categories.stream()
                                .mapToInt(e -> e.questions.stream().mapToInt(f -> f.singleOptions.size()).sum()).sum());

        // check few values
        AssessmentCategory assessCategory = assessment.assessmentQuestionnaire.categories.stream().findFirst().get();
        Category category = questionnaire.categories.stream().findFirst().get();
        assertThat(assessCategory.name.equals(category.name) && assessCategory.order == category.order).isTrue();
        assertThat(assessCategory.questions.get(assessCategory.questions.size() - 1).questionText)
                .isEqualTo(category.questions.get(category.questions.size() - 1).questionText);
        assertThat(assessCategory.questions.get(assessCategory.questions.size() - 1).singleOptions.get(1).option)
                .isEqualTo(category.questions.get(category.questions.size() - 1).singleOptions.get(1).option);

    }

    private Assessment createAssessment(Long questionnaireId) {
        Assessment assessment = Assessment.builder().applicationId(10L).build();
        assessment.persistAndFlush();

        return assessmentSvc.copyQuestionnaireIntoAssessment(assessment.id, questionnaireId);
    }

    private Questionnaire createQuestionnaire() {
        Questionnaire questionnaire = Questionnaire.builder().name("Test").languageCode("EN").build();
        questionnaire.persistAndFlush();

        questionnaire.categories = IntStream.range(1, new Random().nextInt(15) + 1)
                .mapToObj(e -> createCategory(questionnaire, e)).collect(Collectors.toList());

        return questionnaire;
    }

    private Category createCategory(Questionnaire questionnaire, int order) {
        Category category = Category.builder().name("category-" + order).order(order).questionnaire(questionnaire)
                .build();
        category.persistAndFlush();

        category.questions = IntStream.range(1, new Random().nextInt(15) + 1).mapToObj(e -> createQuestion(category, e))
                .collect(Collectors.toList());
        return category;
    }

    private Question createQuestion(Category category, int i) {
        Question question = Question.builder().name("question-" + i).order(i).questionText("questionText-" + i)
                .description("tooltip-" + i).type("SINGLE").build();
        question.persistAndFlush();

        question.singleOptions = IntStream.range(1, new Random().nextInt(15) + 1)
                .mapToObj(e -> createSingleOption(question, e)).collect(Collectors.toList());

        return question;
    }

    private SingleOption createSingleOption(Question question, int i) {
        SingleOption single = SingleOption.builder().option("option-" + i).order(i).question(question)
                .risk(Risk.values()[new Random().nextInt(Risk.values().length)]).build();
        single.persistAndFlush();
        return single;
    }
}
