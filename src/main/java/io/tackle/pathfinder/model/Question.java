package io.tackle.pathfinder.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Root Type for question
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "order",
    "question",
    "tooltip",
    "answers"
})
public class Question {

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
    @JsonProperty("question")
    @JsonPropertyDescription("")
    private String question;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tooltip")
    @JsonPropertyDescription("")
    private String tooltip;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answers")
    @JsonPropertyDescription("")
    private List<Answer> answers = new ArrayList<Answer>();

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
    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("question")
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tooltip")
    public String getTooltip() {
        return tooltip;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("tooltip")
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answers")
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answers")
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
