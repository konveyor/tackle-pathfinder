package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "date",
    "user",
    "status",
    "categories"
})
@Data
public class AssessmentDto {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("date")
    @JsonPropertyDescription("")
    private String date;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("user")
    @JsonPropertyDescription("")
    private String user;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    @JsonPropertyDescription("")
    private AssessmentDto.Status status;

    public enum Status {

        EMPTY("EMPTY"),
        STARTED("STARTED"),
        COMPLETE("COMPLETE"),
        DISCARDED("DISCARDED");
        private final String value;
        private static final Map<String, AssessmentDto.Status> CONSTANTS = new HashMap<>();

        static {
            for (AssessmentDto.Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Status(String value) {
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
        public static AssessmentDto.Status fromValue(String value) {
            AssessmentDto.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
