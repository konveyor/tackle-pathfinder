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
    "name",
    "tooltip",
    "question",
    "comment",
    "answers"
})
public class AssessQuestion {

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
    @JsonProperty("tooltip")
    @JsonPropertyDescription("")
    private String tooltip;
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
    @JsonProperty("comment")
    @JsonPropertyDescription("")
    private String comment;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answers")
    @JsonPropertyDescription("")
    private List<AssessAnswer> answers = new ArrayList<AssessAnswer>();

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
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answers")
    public List<AssessAnswer> getAnswers() {
        return answers;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("answers")
    public void setAnswers(List<AssessAnswer> answers) {
        this.answers = answers;
    }

}
