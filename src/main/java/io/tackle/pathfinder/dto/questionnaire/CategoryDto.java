
package io.tackle.pathfinder.dto.questionnaire;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.tackle.pathfinder.dto.BasicDto;
import io.tackle.pathfinder.model.questionnaire.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Root Type for section
 * <p>
 * Questionnaire section
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "order",
    "title",
    "questions"
})
@Generated("jsonschema2pojo")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class CategoryDto extends BasicDto {
    /**
     * Used for sorting multiple sections
     * (Required)
     * 
     */
    @JsonProperty("order")
    @JsonPropertyDescription("Used for sorting multiple sections")
    private Integer order;
    /**
     * Title of the section. This will be represented as a Wizard's section
     * (Required)
     * 
     */
    @JsonProperty("title")
    @JsonPropertyDescription("Title of the section. This will be represented as a Wizard's section")
    private String title;
    /**
     * List of questions that belongs to this section
     * (Required)
     * 
     */
    @JsonProperty("questions")
    @JsonPropertyDescription("List of questions that belongs to this section")
    private List<QuestionDto> questions = new ArrayList<QuestionDto>();
}
