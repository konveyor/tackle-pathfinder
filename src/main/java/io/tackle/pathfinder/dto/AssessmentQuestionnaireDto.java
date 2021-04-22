
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
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "categories",
    "title",
    "description",
    "language"
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class AssessmentQuestionnaireDto {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("categories")
    @JsonPropertyDescription("")
    @Builder.Default
    private List<AssessmentCategoryDto> categories = new ArrayList<AssessmentCategoryDto>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    @JsonPropertyDescription("")
    private String title;
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
}
