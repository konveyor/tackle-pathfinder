package io.tackle.pathfinder.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Root Type for answer
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "order",
    "answer",
    "risk"
})
public class Answer {

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
    @JsonProperty("answer")
    @JsonPropertyDescription("")
    private String answer;
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

    public enum Risk {

        GREEN("GREEN"),
        AMBER("AMBER"),
        RED("RED");
        private final String value;
        private final static Map<String, Answer.Risk> CONSTANTS = new HashMap<String, Answer.Risk>();

        static {
            for (Answer.Risk c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Risk(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Answer.Risk fromValue(String value) {
            Answer.Risk constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
