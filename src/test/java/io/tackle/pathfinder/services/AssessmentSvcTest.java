package io.tackle.pathfinder.services;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.pathfinder.model.QuestionType;
import io.tackle.pathfinder.model.Risk;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentQuestionnaire;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import io.tackle.pathfinder.model.questionnaire.Category;
import io.tackle.pathfinder.model.questionnaire.Question;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import io.tackle.pathfinder.model.questionnaire.SingleOption;
import lombok.extern.java.Log;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@QuarkusTestResource(value = PostgreSQLDatabaseTestResource.class,
        initArgs = {
                @ResourceArg(name = PostgreSQLDatabaseTestResource.DB_NAME, value = "pathfinder_db"),
                @ResourceArg(name = PostgreSQLDatabaseTestResource.USER, value = "pathfinder"),
                @ResourceArg(name = PostgreSQLDatabaseTestResource.PASSWORD, value = "pathfinder")
        }
)
@QuarkusTest
@Log
public class AssessmentSvcTest {
    @Inject
    AssessmentSvc assessmentSvc; 

    @Inject
    ManagedExecutor managedExecutor;

    @Test
    @Transactional
    public void given_Questionnaire_when_CopyQuestionnaireIntoAssessment_should_BeIdentical() throws InterruptedException {
        Questionnaire questionnaire = createQuestionnaire();
        List<Category> categories = questionnaire.categories;

        Assessment assessment = createAssessment(questionnaire, 10L);
        AssessmentQuestionnaire assessmentQuestionnaire = assessment.assessmentQuestionnaire;
        List<AssessmentCategory> categoriesAssessQuestionnaire = assessmentQuestionnaire.categories;

        assertThat(assessment.stakeholdergroups.size()).isEqualTo(2);
        assertThat(assessment.stakeholders.size()).isEqualTo(3);
        assertThat(categoriesAssessQuestionnaire.size()).isGreaterThan(0);
        assertThat(categories.size()).isGreaterThan(0);
        assertThat(assessmentQuestionnaire.categories.size()).isEqualTo(categories.size());

        // same questions
        assertThat(assessmentQuestionnaire.categories.stream()
                .collect(Collectors.summarizingInt(p -> p.questions.size())).getSum())
                        .isEqualTo(categories.stream()
                                .collect(Collectors.summarizingInt(p -> p.questions.size())).getSum());

        // same options
        assertThat(assessmentQuestionnaire.categories.stream()
                .mapToInt(e -> e.questions.stream().mapToInt(f -> f.singleOptions.size()).sum()).sum())
                        .isEqualTo(categories.stream()
                                .mapToInt(e -> e.questions.stream().mapToInt(f -> f.singleOptions.size()).sum()).sum());

        // check few values
        AssessmentCategory assessFirstCategory = categoriesAssessQuestionnaire.stream().sorted(Comparator.comparing(a -> a.order)).findFirst().get();
        Category firstCategory = categories.stream().sorted(Comparator.comparing(a -> a.order)).findFirst().get();

        assertThat(assessFirstCategory.name.equals(firstCategory.name) && assessFirstCategory.order == firstCategory.order).isTrue();

        assertThat(assessFirstCategory.questions.stream().sorted(Comparator.comparing(a -> a.order)).findFirst().get().questionText)
                .isEqualTo(firstCategory.questions.stream().sorted(Comparator.comparing(a -> a.order)).findFirst().get().questionText);

        List<AssessmentSingleOption> aSingleOptions = assessFirstCategory.questions.stream().sorted(Comparator.comparing(a -> a.order)).findFirst().get().singleOptions;
        List<SingleOption> qSingleOptions = firstCategory.questions.stream().sorted(Comparator.comparing(a -> a.order)).findFirst().get().singleOptions;
        assertThat(aSingleOptions.size()).isEqualTo(qSingleOptions.size());
        assertThat(aSingleOptions.stream().sorted(Comparator.comparing(a -> a.order)).findFirst().get().option).isEqualTo(qSingleOptions.stream().sorted(Comparator.comparing(a -> a.order)).findFirst().get().option);

    }

    private void addStakeholdersToAssessment(Assessment assessment) {
        AssessmentStakeholder stakeholder = AssessmentStakeholder.builder().assessment(assessment).stakeholderId(100L).build();
        stakeholder.persist();
        assessment.stakeholders.add(stakeholder);

        stakeholder = AssessmentStakeholder.builder().assessment(assessment).stakeholderId(200L).build();
        stakeholder.persist();
        assessment.stakeholders.add(stakeholder);

        stakeholder = AssessmentStakeholder.builder().assessment(assessment).stakeholderId(300L).build();
        stakeholder.persist();
        assessment.stakeholders.add(stakeholder);

        AssessmentStakeholdergroup group = AssessmentStakeholdergroup.builder().assessment(assessment).stakeholdergroupId(500L).build();
        group.persist();
        assessment.stakeholdergroups.add(group);

        group = AssessmentStakeholdergroup.builder().assessment(assessment).stakeholdergroupId(600L).build();
        group.persist();
        assessment.stakeholdergroups.add(group);
    }

    @Test
    public void given_SameApplication_when_SeveralAssessmentCreation_should_ThrowException() throws InterruptedException {
        Questionnaire questionnaire = createQuestionnaire();
        CompletableFuture<Assessment> future1 = managedExecutor.supplyAsync(() -> createAssessment(questionnaire, 5L));
        Thread.sleep(500);
        CompletableFuture<Assessment> future4 = managedExecutor.supplyAsync(() -> createAssessment(questionnaire, 5L));
        assertThat(future1).succeedsWithin(Duration.ofSeconds(10)).matches(e -> e.id > 0);
        assertThat(future4).failsWithin(Duration.ofSeconds(1));
    }    
    
    @Test
    public void given_SameApplicationButDeletedTrue_when_SeveralAssessmentCreation_should_NotThrowException() {
        Questionnaire questionnaire = createQuestionnaire();
        Assessment assessment1 = createAssessment(questionnaire, 200L);
        assertThat(assessment1).matches(e -> e.id > 0);
        assertThatThrownBy(() -> createAssessment(questionnaire, 200L));
        deleteAssessment(assessment1.id);  
        Assessment assessment2 = createAssessment(questionnaire, 200L);
        assertThat(assessment2).matches(e -> e.id > 0);
    }

    @Transactional
    public void deleteAssessment(Long assessmentId) {
        Assessment.findById(assessmentId).delete();
    }

    @Transactional
    public Assessment createAssessment(Questionnaire questionnaire, long applicationId) {
        log.info("Creating an assessment ");
        Assessment assessment = Assessment.builder().applicationId(applicationId).build();
        assessment.persistAndFlush();

        addStakeholdersToAssessment(assessment);

        return assessmentSvc.copyQuestionnaireIntoAssessment(assessment, questionnaire);
    }

    @Transactional
    public Questionnaire createQuestionnaire() {
        Questionnaire questionnaire = Questionnaire.builder().name("Test").languageCode("EN").build();
        questionnaire.persistAndFlush();

        questionnaire.categories = IntStream.range(1, 10).mapToObj(e -> createCategory(questionnaire, e)).collect(Collectors.toList());

        return questionnaire;
    }

    private Category createCategory(Questionnaire questionnaire, int order) {
        Category category = Category.builder().name("category-" + order).order(order).questionnaire(questionnaire)
                .build();
        category.persistAndFlush();

        category.questions = IntStream.range(1, 20).mapToObj(e -> createQuestion(category, e))
                .collect(Collectors.toList());
        return category;
    }

    private Question createQuestion(Category category, int i) {
        Question question = Question.builder().name("question-" + i).order(i).questionText("questionText-" + i)
                .description("tooltip-" + i).type(QuestionType.SINGLE).build();
        question.persistAndFlush();

        question.singleOptions = IntStream.range(1, new Random().nextInt(10) + 3)
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
