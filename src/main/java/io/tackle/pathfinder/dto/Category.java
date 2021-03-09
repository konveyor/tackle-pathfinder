package io.tackle.pathfinder.dto;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Root Type for category
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "order",
    "questions"
})
public class Category {

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
    @JsonProperty("order")
    @JsonPropertyDescription("")
    private Integer order;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("questions")
    @JsonPropertyDescription("")
    private List<Question> questions = new ArrayList<Question>();

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
    @JsonProperty("questions")
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("questions")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}
