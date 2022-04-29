/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
