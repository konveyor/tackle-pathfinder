
package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.tackle.pathfinder.model.Risk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Root Type for question_option
 * <p>
 * A possible question's answer typically represented in a RadioButton
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "order",
    "option",
    "checked"
})
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class AssessmentQuestionOptionDto extends BasicDto {
    /**
     * For sorting multiple radioButtons/checkboxes
     * (Required)
     * 
     */
    @JsonProperty("order")
    @JsonPropertyDescription("For sorting multiple radioButtons/checkboxes")
    private Integer order;
    /**
     * The radioButton/checkbox text
     * (Required)
     * 
     */
    @JsonProperty("option")
    @JsonPropertyDescription("The radioButton/checkbox text")
    private String option;
    /**
     * Whether or not this option was selected by the user
     * (Required)
     * 
     */
    @JsonProperty("checked")
    @JsonPropertyDescription("Whether or not this option was selected by the user")
    private Boolean checked;

    /**
     * Risk associated to this option
     * (Required)
     *
     */
    @JsonProperty("risk")
    @JsonPropertyDescription("Risk associated to this option")
    private Risk risk;

}
