package io.tackle.pathfinder.mapper;

import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.model.assessment.Assessment;
import io.tackle.pathfinder.model.assessment.AssessmentCategory;
import io.tackle.pathfinder.model.assessment.AssessmentQuestion;
import io.tackle.pathfinder.model.assessment.AssessmentQuestionnaire;
import io.tackle.pathfinder.model.assessment.AssessmentSingleOption;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholder;
import io.tackle.pathfinder.model.assessment.AssessmentStakeholdergroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
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

    default List<RiskLineDto> riskListQueryToRiskLineDtoList(List<Object[]> objectList) {
        List<RiskLineDto> riskLineDtos = objectList.stream().map(e -> getRiskLineDto(e))
                                         .collect(Collectors.toList());
        return riskLineDtos;
    }

    private RiskLineDto getRiskLineDto(Object[] fields) {
        // cat.category_order, cat.name, q.question_order, q.question_text, opt.singleoption_order, opt.option, array_agg(a.application_id)
        String fieldApps = (String) fields[6];
        String[] appsList = fieldApps.replace("{", "").replace("}", "").split(",");
        List<Long> applications = Arrays.stream(appsList).map(Long::parseLong).collect(Collectors.toList());
        return RiskLineDto.builder()
                .category((String) fields[1])
                .question((String) fields[3])
                .answer((String) fields[5])
                .applications(applications)
                .build();
    }
}
