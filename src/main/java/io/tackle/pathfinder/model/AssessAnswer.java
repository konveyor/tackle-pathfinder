package io.tackle.pathfinder.model;

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
    "risk",
    "selected",
    "answer"
})
public class AssessAnswer {

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
    @JsonProperty("risk")
    @JsonPropertyDescription("")
    private Answer.Risk risk;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("selected")
    @JsonPropertyDescription("")
    private Boolean selected;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answer")
    @JsonPropertyDescription("")
    private String answer;

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
    @JsonProperty("risk")
    public Answer.Risk getRisk() {
        return risk;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("risk")
    public void setRisk(Answer.Risk risk) {
        this.risk = risk;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("selected")
    public Boolean getSelected() {
        return selected;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("selected")
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answer")
    public String getAnswer() {
        return answer;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answer")
    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
