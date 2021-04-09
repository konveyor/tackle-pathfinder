package io.tackle.pathfinder.mapper;

import io.tackle.pathfinder.dto.AssessmentCategoryDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.dto.AssessmentQuestionDto;
import io.tackle.pathfinder.dto.AssessmentQuestionOptionDto;
import io.tackle.pathfinder.dto.AssessmentQuestionnaireDto;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentQuestion;
import io.tackle.pathfinder.model.assessment.AssessmentQuestionnaire;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface AssessmentMapper {

    AssessmentHeaderDto assessmentToAssessmentHeaderDto(Assessment assessment);

    AssessmentQuestionOptionDto assessmentSingleOptionToAssessmentQuestionOptionDto(AssessmentSingleOption option);
    List<AssessmentQuestionOptionDto> assessmentSingleOptionListToassessmentQuestionOptionDtoList(List<AssessmentSingleOption> optionList);
    @Mapping(target = "options", source="singleOptions")
    AssessmentQuestionDto assessmentQuestionToAssessmentQuestionDto(AssessmentQuestion question);
    List<AssessmentQuestionDto> assessmentQuestionListToassessmentQuestionDtoList(List<AssessmentQuestion> questionList);
    @Mapping(target="title", source="name")
    AssessmentCategoryDto assessmentCategoryToAssessmentCategoryDto(AssessmentCategory category);
    List<AssessmentCategoryDto> assessmentCategoryListToAssessmentCategoryDtoList(List<AssessmentCategory> categoryList);
    
    @Mapping(target="title", source="name")
    @Mapping(target="language", source="languageCode")
    AssessmentQuestionnaireDto assessmentQuestionnaireToAssessmentQuestionnaireDto(AssessmentQuestionnaire questionnaire);

    @Mapping(target = "questionnaire", source = "assessmentQuestionnaire")
    AssessmentDto assessmentToAssessmentDto(Assessment assessment);
}
