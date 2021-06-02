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
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import lombok.extern.java.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface AssessmentMapper {

    AssessmentHeaderDto assessmentToAssessmentHeaderDto(Assessment assessment);

    @Mapping(target="checked", source="selected")
    AssessmentQuestionOptionDto assessmentSingleOptionToAssessmentQuestionOptionDto(AssessmentSingleOption option);
    List<AssessmentQuestionOptionDto> assessmentSingleOptionListToassessmentQuestionOptionDtoList(List<AssessmentSingleOption> optionList);
    
    @Mapping(target = "options", source="singleOptions")
    @Mapping(target = "question", source="questionText")
    AssessmentQuestionDto assessmentQuestionToAssessmentQuestionDto(AssessmentQuestion question);
    List<AssessmentQuestionDto> assessmentQuestionListToassessmentQuestionDtoList(List<AssessmentQuestion> questionList);
   
    @Mapping(target="title", source="name")
    AssessmentCategoryDto assessmentCategoryToAssessmentCategoryDto(AssessmentCategory category);
    List<AssessmentCategoryDto> assessmentCategoryListToAssessmentCategoryDtoList(List<AssessmentCategory> categoryList);
    
    @Mapping(target="title", source="name")
    @Mapping(target="language", source="languageCode")
    AssessmentQuestionnaireDto assessmentQuestionnaireToAssessmentQuestionnaireDto(AssessmentQuestionnaire questionnaire);

    default List<Long> assessmentStakeholderListToLongList(List<AssessmentStakeholder> stakeholder) {
        return stakeholder.stream().map(e -> e.stakeholderId).collect(Collectors.toList());
    }
    default List<Long> assessmentStakeholderGroupListToLongList(List<AssessmentStakeholdergroup> stakeholdergroup) {
        return stakeholdergroup.stream().map(e -> e.stakeholdergroupId).collect(Collectors.toList());
    }

    @Mapping(target = "questionnaire", source = "assessmentQuestionnaire")
    @Mapping(target = "stakeholderGroups", source = "stakeholdergroups")
    AssessmentDto assessmentToAssessmentDto(Assessment assessment);

    default String stringToTranslatedString(String value) {
        return "XX-" + value;
    }
}
