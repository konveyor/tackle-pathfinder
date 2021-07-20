
package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.tackle.pathfinder.services.TranslatorSvc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


/**
 * Root Type for assessment
 * <p>
 * The application's assessment
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "applicationId",
    "status",
    "stakeholders",
    "stakeholderGroups",
    "questionnaire"
})
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class AssessmentDto extends BasicDto {
    /**
     * Application id
     * 
     */
    @JsonProperty("applicationId")
    @JsonPropertyDescription("Application id")
    private Long applicationId;
    /**
     * Current status of the assessment
     * 
     */
    @JsonProperty("status")
    @JsonPropertyDescription("Current status of the assessment")
    private AssessmentStatus status;
    /**
     * List of ids of stakeholders
     * 
     */
    @JsonProperty("stakeholders")
    @JsonPropertyDescription("List of ids of stakeholders")
    private List<Long> stakeholders; 
    /**
     * List of ids of stakeholder groups
     * 
     */
    @JsonProperty("stakeholderGroups")
    @JsonPropertyDescription("List of ids of stakeholder groups")
    private List<Long> stakeholderGroups; 
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("questionnaire")
    @JsonPropertyDescription("")
    private AssessmentQuestionnaireDto questionnaire;

    
}
