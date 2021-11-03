
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
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Root Type for question
 * <p>
 * A question
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "order",
    "question",
    "options",
    "description"
})
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class AssessmentQuestionDto extends BasicDto {
    /**
     * Used for sorting multiple questions
     * (Required)
     * 
     */
    @JsonProperty("order")
    @JsonPropertyDescription("Used for sorting multiple questions")
    private Integer order;
    /**
     * Question's value typically ends with '?'
     * (Required)
     * 
     */
    @JsonProperty("question")
    @JsonPropertyDescription("Question's value typically ends with '?'")
    private String question;
    /**
     * List of possible answers to the question. Applicable if variant=checkbox|radioButton.
     * (Required)
     * 
     */
    @JsonProperty("options")
    @JsonPropertyDescription("List of possible answers to the question. Applicable if variant=checkbox|radioButton.")
    @Builder.Default
    private List<AssessmentQuestionOptionDto> options = new ArrayList<AssessmentQuestionOptionDto>();
    /**
     * Extra info. This will appear in the tooltip
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("Extra info. This will appear in the tooltip")
    private String description;

}
