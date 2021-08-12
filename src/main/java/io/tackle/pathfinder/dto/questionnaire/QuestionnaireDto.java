
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
import io.tackle.pathfinder.model.questionnaire.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "categories",
    "title",
    "description",
    "language"
})
@Generated("jsonschema2pojo")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class QuestionnaireDto extends BasicDto {
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("name")
    @JsonPropertyDescription("")
    private String name;
    /**
     *
     */
    @JsonProperty("description")
    @JsonPropertyDescription("")
    private String description;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("language")
    @JsonPropertyDescription("")
    private String language;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("categories")
    @JsonPropertyDescription("")
    private List<CategoryDto> categories = new ArrayList<CategoryDto>();
}
