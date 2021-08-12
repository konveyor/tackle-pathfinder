
package io.tackle.pathfinder.dto.questionnaire;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.tackle.pathfinder.dto.BasicDto;
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
    "checked",
    "risk"
})
@Generated("jsonschema2pojo")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class QuestionOptionDto extends BasicDto {
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
     * 
     * (Required)
     * 
     */
    @JsonProperty("risk")
    @JsonPropertyDescription("")
    private Risk risk;
}
