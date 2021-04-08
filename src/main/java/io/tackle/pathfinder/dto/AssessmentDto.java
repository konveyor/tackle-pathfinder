
package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection

public class AssessmentDto {

    /**
     * Assessment id
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("Assessment id")
    private Integer id;
    /**
     * Application id
     * 
     */
    @JsonProperty("applicationId")
    @JsonPropertyDescription("Application id")
    private Integer applicationId;
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
    @Builder.Default
    private List<Integer> stakeholders = new ArrayList<Integer>();
    /**
     * List of ids of stakeholder groups
     * 
     */
    @JsonProperty("stakeholderGroups")
    @JsonPropertyDescription("List of ids of stakeholder groups")
    @Builder.Default
    private List<Integer> stakeholderGroups = new ArrayList<Integer>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("questionnaire")
    @JsonPropertyDescription("")
    private AssessmentQuestionnaireDto questionnaire;

    
}
