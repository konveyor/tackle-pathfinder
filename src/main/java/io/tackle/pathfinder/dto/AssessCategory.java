package io.tackle.pathfinder.dto;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "order",
    "name",
    "questions"
})
public class AssessCategory {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("order")
    @JsonPropertyDescription("")
    private Integer order;
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
     * (Required)
     * 
     */
    @JsonProperty("questions")
    @JsonPropertyDescription("")
    private List<AssessQuestion> questions = new ArrayList<AssessQuestion>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("order")
    public Integer getOrder() {
        return order;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("order")
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("questions")
    public List<AssessQuestion> getQuestions() {
        return questions;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("questions")
    public void setQuestions(List<AssessQuestion> questions) {
        this.questions = questions;
    }

}
