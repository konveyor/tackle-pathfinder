package io.tackle.pathfinder.services;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.Mock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;
import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.mapper.AssessmentMapper;
import io.tackle.pathfinder.model.QuestionType;
import io.tackle.pathfinder.model.Risk;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentQuestion;
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
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.transaction.*;
import javax.ws.rs.BadRequestException;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    @Inject
    AssessmentMapper assessmentMapper;

    @Inject
    UserTransaction transaction;

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

    @Test
    @Transactional
    public void given_CreatedAssessment_When_Update_Then_ItChangesOnlyThePartSent() throws InterruptedException {
        Assessment assessment = createAssessment(Questionnaire.findAll().firstResult(), 1410L);

        assertThat(assessment.stakeholdergroups).hasSize(2);
        assertThat(assessment.stakeholders).hasSize(3);

        AssessmentDto assessmentDto = assessmentMapper.assessmentToAssessmentDto(assessment);
        assertThat(assessmentDto.getStakeholderGroups()).hasSize(2);
        assertThat(assessmentDto.getStakeholders()).hasSize(3); 

        // Stakeholders and Stakeholdergroups always override the entire list
        assessmentDto.getStakeholderGroups().clear();
        assessmentDto.getStakeholderGroups().add(8100L);
        assessmentDto.getStakeholderGroups().add(8200L);

        assessmentDto.getStakeholders().clear();
        assessmentDto.getStakeholders().add(8500L);
        assessmentDto.getStakeholders().add(8600L);

        AssessmentCategoryDto assessmentCategoryDto = assessmentDto.getQuestionnaire().getCategories().get(0);
        assessmentCategoryDto.setComment("This is a test comment");
        AssessmentQuestionDto assessmentQuestionDto = assessmentCategoryDto.getQuestions().get(0);
        AssessmentQuestionOptionDto assessmentQuestionOptionDto = assessmentQuestionDto.getOptions().get(0);
        assessmentQuestionOptionDto.setChecked(true);

        assessmentSvc.updateAssessment(assessment.id, assessmentDto);

        Assessment assessmentUpdated = Assessment.findById(assessment.id);
        assertThat(assessmentUpdated.stakeholders).extracting(e -> e.stakeholderId).containsExactlyInAnyOrder(8500L, 8600L);
        assertThat(assessmentUpdated.stakeholdergroups).extracting(e -> e.stakeholdergroupId).containsExactlyInAnyOrder(8100L, 8200L);

        assertThat(assessmentUpdated.assessmentQuestionnaire.categories).extracting(e -> e.comment)
                .containsOnlyOnce("This is a test comment");

        assertThat(getCheckedForOption(assessmentUpdated, assessmentCategoryDto.getId(), assessmentQuestionDto.getId(),
                assessmentQuestionOptionDto.getId())).isTrue();
    }

    @Test
    @Transactional
    public void given_CreatedAssessment_When_UpdateWithStakeholdersNull_Then_ItDoesntChangeStakeholders() throws InterruptedException {
        Assessment assessment = createAssessment(Questionnaire.findAll().firstResult(), 2410L);

        assertThat(assessment.stakeholders).extracting(e -> e.stakeholderId).containsExactlyInAnyOrder(100L, 200L, 300L);
        assertThat(assessment.stakeholdergroups).extracting(e -> e.stakeholdergroupId).containsExactlyInAnyOrder(500L, 600L);

        AssessmentDto assessmentDto = assessmentMapper.assessmentToAssessmentDto(assessment);
        assertThat(assessmentDto.getStakeholderGroups()).hasSize(2);
        assertThat(assessmentDto.getStakeholders()).hasSize(3); 

        // Stakeholders and Stakeholdergroups NOT send will imply leave what is there without touching it
        assessmentDto.setStakeholderGroups(null);
        assessmentDto.setStakeholders(null);

        assessmentSvc.updateAssessment(assessment.id, assessmentDto);

        Assessment assessmentUpdated = Assessment.findById(assessment.id);
        assertThat(assessmentUpdated.stakeholders).extracting(e -> e.stakeholderId).containsExactlyInAnyOrder(100L, 200L, 300L);
        assertThat(assessmentUpdated.stakeholdergroups).extracting(e -> e.stakeholdergroupId).containsExactlyInAnyOrder(500L, 600L);
    }

    @Test
    @Transactional
    public void given_CreatedAssessment_When_UpdateSeveralTimes_Then_StakeholdersAreNotDuplicated()  {
        Assessment assessment = createAssessment(Questionnaire.findAll().firstResult(), 2415L);

        assertThat(assessment.stakeholders).extracting(e -> e.stakeholderId).containsExactlyInAnyOrder(100L, 200L, 300L);
        assertThat(assessment.stakeholdergroups).extracting(e -> e.stakeholdergroupId).containsExactlyInAnyOrder(500L, 600L);

        AssessmentDto assessmentDto = assessmentMapper.assessmentToAssessmentDto(assessment);
        assertThat(assessmentDto.getStakeholderGroups()).hasSize(2);
        assertThat(assessmentDto.getStakeholders()).hasSize(3); 

        // Stakeholders and Stakeholdergroups NOT send will imply leave what is there without touching it
        assessmentDto.setStakeholderGroups(null);
        assessmentDto.setStakeholders(null);

        assessmentSvc.updateAssessment(assessment.id, assessmentDto);

        Assessment assessmentUpdated = Assessment.findById(assessment.id);
        assertThat(assessmentUpdated.stakeholders).extracting(e -> e.stakeholderId).containsExactlyInAnyOrder(100L, 200L, 300L);
        assertThat(assessmentUpdated.stakeholdergroups).extracting(e -> e.stakeholdergroupId).containsExactlyInAnyOrder(500L, 600L);
        
        // adding again the same original list to the dtos, should not change anything as we are adding the same ones it contains
        assessmentDto.setStakeholders(List.of(180L, 200L, 300L, 800L));
        assessmentDto.setStakeholderGroups(List.of(550L, 650L, 750L));

        assessmentSvc.updateAssessment(assessment.id, assessmentDto);

        assessmentUpdated = Assessment.findById(assessment.id);
        assertThat(assessmentUpdated.stakeholders).extracting(e -> e.stakeholderId).containsExactlyInAnyOrder(180L, 200L, 300L, 800L);
        assertThat(assessmentUpdated.stakeholdergroups).extracting(e -> e.stakeholdergroupId).containsExactlyInAnyOrder(550L, 650L, 750L);
        
        assessmentSvc.updateAssessment(assessment.id, assessmentDto);

        assessmentUpdated = Assessment.findById(assessment.id);
        assertThat(assessmentUpdated.stakeholders).extracting(e -> e.stakeholderId).containsExactlyInAnyOrder(180L, 200L, 300L, 800L);
        assertThat(assessmentUpdated.stakeholdergroups).extracting(e -> e.stakeholdergroupId).containsExactlyInAnyOrder(550L, 650L, 750L);
    }    
    
    @Test
    @Transactional
    public void given_CreatedAssessment_When_UpdateWithStakeholdersEmpty_Then_ItDeleteAllStakeholders() throws InterruptedException {
        Assessment assessment = createAssessment(Questionnaire.findAll().firstResult(), 3410L);

        assertThat(assessment.stakeholders).extracting(e -> e.stakeholderId).containsExactlyInAnyOrder(100L, 200L, 300L);
        assertThat(assessment.stakeholdergroups).extracting(e -> e.stakeholdergroupId).containsExactlyInAnyOrder(500L, 600L);

        AssessmentDto assessmentDto = assessmentMapper.assessmentToAssessmentDto(assessment);
        assertThat(assessmentDto.getStakeholderGroups()).hasSize(2);
        assertThat(assessmentDto.getStakeholders()).hasSize(3); 

        // Stakeholders and Stakeholdergroups sent EMPTY send will imply deleting every element in DB in stakeholders/stakeholdergroups
        assessmentDto.getStakeholderGroups().clear();
        assessmentDto.getStakeholders().clear();

        assessmentSvc.updateAssessment(assessment.id, assessmentDto);

        Assessment assessmentUpdated = Assessment.findById(assessment.id);
        assertThat(assessmentUpdated.stakeholders).hasSize(0);
        assertThat(assessmentUpdated.stakeholdergroups).hasSize(0);
    }
    
    @Transactional
    private boolean getCheckedForOption(Assessment assessment, Long categoryId, Long questionId, Long optionId) throws InterruptedException {
        log.info("categories to check " + assessment.assessmentQuestionnaire.categories.stream().count());
        log.info("categories to check " + assessment.assessmentQuestionnaire.categories.stream().map(e -> e.id.toString()).collect(Collectors.joining(" ## ")));

        log.info("ids " + categoryId + "--" + questionId + "--" + optionId);
        AssessmentCategory category = assessment.assessmentQuestionnaire.categories.stream()
                .filter(e -> e.id.equals(categoryId)).findFirst().orElseThrow();

        AssessmentQuestion question = category.questions.stream().filter(e -> e.id.equals(questionId)).findFirst()
                .orElseThrow();
        AssessmentSingleOption option = question.singleOptions.stream().filter(e -> e.id.equals(optionId)).findFirst()
                .orElseThrow();
        return option.selected;
    }

    @Transactional
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
    @Transactional
    public void given_SameApplication_when_SeveralAssessmentCreation_should_ThrowException() throws InterruptedException {
        Questionnaire questionnaire = createQuestionnaire();
        CompletableFuture<Assessment> future1 = managedExecutor.supplyAsync(() -> createAssessment(questionnaire, 5L));
        Thread.sleep(500);
        CompletableFuture<Assessment> future4 = managedExecutor.supplyAsync(() -> createAssessment(questionnaire, 5L));
        assertThat(future1).succeedsWithin(Duration.ofSeconds(10)).matches(e -> e.id > 0);
        assertThat(future4).failsWithin(Duration.ofSeconds(1));
    }

    @Test
    public void given_SameApplicationButDeletedTrue_when_SeveralAssessmentCreation_should_NotThrowException() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        transaction.begin();
        Questionnaire questionnaire = createQuestionnaire();
        Assessment assessment1 = createAssessment(questionnaire, 6200L);
        assertThat(assessment1).matches(e -> e.id > 0);
        transaction.commit();
        assertThatThrownBy(() -> createAssessment(questionnaire, 6200L));
        assessmentSvc.deleteAssessment(assessment1.id);
        transaction.begin();
        Assessment assessment2 = createAssessment(questionnaire, 6200L);
        assertThat(assessment2).matches(e -> e.id > 0);
        transaction.commit();
    }

    @Test
    @Transactional
    public void given_CreatedAssessment_When_DeleteAssessment_should_DeleteIt() {
        Questionnaire questionnaire = createQuestionnaire();
        Assessment assessment = createAssessment(questionnaire, 1200L);
        assessmentSvc.deleteAssessment(assessment.id);
        assertThat(Assessment.findByIdOptional(assessment.id)).isEmpty();
    }    
    
    @Test
    public void given_NotExistingAssessment_When_DeleteAssessment_should_ThrowException() {
        assertThatThrownBy(() -> assessmentSvc.deleteAssessment(8888L));
    }    
    
    @Test
    @Transactional
    public void given_AlreadyDeletedAssessment_When_DeleteAssessment_should_ThrowException() {
        Questionnaire questionnaire = createQuestionnaire();
        Assessment assessment = createAssessment(questionnaire, 897200L);
        
        assertThat(Assessment.findByIdOptional(assessment.id)).isNotEmpty();
        
        assessmentSvc.deleteAssessment(assessment.id);

        assertThat(Assessment.findByIdOptional(assessment.id)).isEmpty();
        assertThatThrownBy(() -> assessmentSvc.deleteAssessment(assessment.id));
    } 
    
    @Test
    @Transactional
    public void given_Assessment_When_DeleteAssessmentAndFails_should_ThrowException() {
        Questionnaire questionnaire = createQuestionnaire();
        Assessment assessment = createAssessment(questionnaire, 897200L);
        
        assertThat(Assessment.findByIdOptional(assessment.id)).isNotEmpty();

        assessmentSvc.deleteAssessment(assessment.id);

        assertThat(Assessment.findByIdOptional(assessment.id)).isEmpty();
        assertThatThrownBy(() -> assessmentSvc.deleteAssessment(8888L));
    }

    @Test
    @Transactional
    public void given_AlreadyAssessedApplication_When_CopyAssessment_ShouldCopyAllUserFieldValues() {
        Questionnaire questionnaire = createQuestionnaire();
        Assessment assessment = createAssessment(questionnaire, 8897200L);
        assessment.assessmentQuestionnaire.categories.get(0).comment = "My special comment";
        assessment.assessmentQuestionnaire.categories.get(0).questions.get(0).singleOptions.get(0).selected = true;
        assessment.status = AssessmentStatus.COMPLETE;

        AssessmentHeaderDto copyHeader = assessmentSvc.copyAssessment(assessment.id, 9997200L);
        Assessment assessmentCopied = Assessment.findById(copyHeader.getId());

        AssessmentDto assessmentSourceDto = assessmentMapper.assessmentToAssessmentDto(assessment);
        AssessmentDto assessmentTargetDto = assessmentMapper.assessmentToAssessmentDto(assessmentCopied);
        		// Compare Values
		assertThat(assessmentTargetDto)
        .usingRecursiveComparison()
            .ignoringFieldsMatchingRegexes(".*\\.id",".*Id", ".*create.*", "update.*", "id")
            .ignoringCollectionOrder().isEqualTo(assessmentSourceDto);
        // We'll mock Panache Entity Assessment to return false when doing a deleteById to force the exception
        PanacheMock.mock(Assessment.class);
        PanacheMock.doCallRealMethod().when(Assessment.class).findByIdOptional(Mockito.any());
        PanacheMock.doReturn(false).when(Assessment.class).deleteById(Mockito.any());

        assertThatThrownBy(() -> assessmentSvc.deleteAssessment(assessment.id)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @Transactional
    public void given_AssessmentWithRedAnswers_When_landscape_Then_ResultIsRED() {
        // create 2 assessments
        AssessmentHeaderDto assessmentHeader1 = assessmentSvc.createAssessment( 899200L);
        AssessmentHeaderDto assessmentHeader2 = assessmentSvc.createAssessment( 898200L);
        Assessment assessment1 =  Assessment.findById(assessmentHeader1.getId());
        Assessment assessment2 =  Assessment.findById(assessmentHeader2.getId());

        // update both, selecting first RED option
        assessment1.status = AssessmentStatus.COMPLETE;
        assessment1.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.RED).findFirst().ifPresent(b -> b.selected = true)));
        assessment2.status = AssessmentStatus.COMPLETE;
        assessment2.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.RED).findFirst().ifPresent(b -> b.selected = true)));

        // get landscape report of both
        List<LandscapeDto> landscape = assessmentSvc.landscape(List.of(899200L, 898200L));

        // asserts
        assertThat(landscape).containsExactly(new LandscapeDto(assessment1.id, Risk.RED), new LandscapeDto(assessment2.id, Risk.RED));
    }

    @Test
    @Transactional
    public void given_AssessmentWithNoRedAnswersFewAmber_When_landscape_Then_ResultIsGREEN() {
        // create 2 assessments
        AssessmentHeaderDto assessmentHeader1 = assessmentSvc.createAssessment( 896200L);
        AssessmentHeaderDto assessmentHeader2 = assessmentSvc.createAssessment( 895200L);
        Assessment assessment1 =  Assessment.findById(assessmentHeader1.getId());
        Assessment assessment2 =  Assessment.findById(assessmentHeader2.getId());

        // update both, selecting first GREEN/UNKNOWN option and 1 AMBER and it will remain GREEN
        assessment1.status = AssessmentStatus.COMPLETE;
        assessment1.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.GREEN).findFirst().ifPresent(b -> b.selected = true)));
        assessment2.status = AssessmentStatus.COMPLETE;
        assessment2.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.UNKNOWN).findFirst().ifPresent(b -> b.selected = true)));

        assessment1.assessmentQuestionnaire.categories.get(1).questions.get(1).singleOptions.stream().filter(a -> a.risk == Risk.AMBER).findFirst().ifPresent(b -> b.selected = true);
        assessment2.assessmentQuestionnaire.categories.get(1).questions.get(1).singleOptions.stream().filter(a -> a.risk == Risk.AMBER).findFirst().ifPresent(b -> b.selected = true);

        // get landscape report of both
        List<LandscapeDto> landscape = assessmentSvc.landscape(List.of(896200L, 895200L));

        // asserts
        assertThat(landscape).containsExactly(new LandscapeDto(assessment1.id, Risk.GREEN), new LandscapeDto(assessment2.id, Risk.GREEN));
    }

    @Test
    @Transactional
    public void given_AssessmentWithNoRedAnswersSeveralAmber_When_landscape_Then_ResultIsAMBER() {
        // create 2 assessments
        AssessmentHeaderDto assessmentHeader1 = assessmentSvc.createAssessment(894200L);
        AssessmentHeaderDto assessmentHeader2 = assessmentSvc.createAssessment(893200L);
        Assessment assessment1 =  Assessment.findById(assessmentHeader1.getId());
        Assessment assessment2 =  Assessment.findById(assessmentHeader2.getId());

        // update both, selecting first GREEN option and UNKNOWN
        assessment1.status = AssessmentStatus.COMPLETE;
        assessment1.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.AMBER).findFirst().ifPresent(b -> b.selected = true)));
        assessment2.status = AssessmentStatus.COMPLETE;
        assessment2.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.AMBER).findFirst().ifPresent(b -> b.selected = true)));

        // get landscape report of both
        List<LandscapeDto> landscape = assessmentSvc.landscape(List.of(894200L, 893200L));

        // asserts
        assertThat(landscape).containsExactly(new LandscapeDto(assessment1.id, Risk.AMBER), new LandscapeDto(assessment2.id, Risk.AMBER));
    }

    @Test
    @Transactional
    public void given_AssessmentsNotCompleted_When_landscape_Then_ThoseAssessmentsAreNotIncludedInResult() {
        // create 2 assessments
        AssessmentHeaderDto assessmentHeader1 = assessmentSvc.createAssessment(1894200L);
        AssessmentHeaderDto assessmentHeader2 = assessmentSvc.createAssessment(1893200L);
        Assessment assessment1 =  Assessment.findById(assessmentHeader1.getId());
        Assessment assessment2 =  Assessment.findById(assessmentHeader2.getId());

        // update both, selecting first GREEN option and UNKNOWN
        assessment1.status = AssessmentStatus.COMPLETE;
        assessment1.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.AMBER).findFirst().ifPresent(b -> b.selected = true)));
        assessment2.assessmentQuestionnaire.categories.forEach(e -> e.questions.forEach(f -> f.singleOptions.stream().filter(a -> a.risk == Risk.AMBER).findFirst().ifPresent(b -> b.selected = true)));

        // get landscape report of both
        List<LandscapeDto> landscape = assessmentSvc.landscape(List.of(1894200L, 1893200L));

        // asserts
        assertThat(landscape).containsExactly(new LandscapeDto(assessment1.id, Risk.AMBER));
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

    @Transactional
    private Category createCategory(Questionnaire questionnaire, int order) {
        Category category = Category.builder()
            .name("category-" + order)
            .order(order)
            .questionnaire(questionnaire)
            .build();
        category.persistAndFlush();

        category.questions = IntStream.range(1, 20).mapToObj(e -> createQuestion(category, e))
                .collect(Collectors.toList());
        return category;
    }

    @Transactional
    private Question createQuestion(Category category, int i) {
        Question question = Question.builder()
            .name("question-" + i)
            .order(i)
            .questionText("questionText-" + i)
            .description("tooltip-" + i)
            .type(QuestionType.SINGLE)
            .category(category)
            .build();
        question.persistAndFlush();

        question.singleOptions = IntStream.range(1, new Random().nextInt(10) + 3)
                .mapToObj(e -> createSingleOption(question, e)).collect(Collectors.toList());

        return question;
    }

    @Transactional
    private SingleOption createSingleOption(Question question, int i) {
        SingleOption single = SingleOption.builder()
            .option("option-" + i)
            .order(i)
            .question(question)
            .risk(Risk.values()[new Random().nextInt(Risk.values().length)])
            .build();
        single.persistAndFlush();
        return single;
    }
}
