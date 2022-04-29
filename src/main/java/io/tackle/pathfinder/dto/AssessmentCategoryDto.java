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
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


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
    "questions",
    "comment"
})
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection

public class AssessmentCategoryDto extends BasicDto {


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
     * Root Type for question
     * <p>
     * A question
     * (Required)
     * 
     */
    @JsonProperty("questions")
    @JsonPropertyDescription("A question")
    private List<AssessmentQuestionDto> questions;
    /**
     * Comment from the user to the category
     * 
     */
    @JsonProperty("comment")
    @JsonPropertyDescription("Comment from the user to the category")
    private String comment;
}
