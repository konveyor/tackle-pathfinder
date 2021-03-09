package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "applicationId",
    "status"
})
public class AssessmentHeader {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("")
    private Integer id;
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
    @JsonProperty("applicationId")
    @JsonPropertyDescription("")
    private Integer application;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    @JsonPropertyDescription("")
    private AssessmentHeader.Status status;

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
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
    @JsonProperty("application")
    public Integer getApplication() {
        return application;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("application")
    public void setApplication(Integer application) {
        this.application = application;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    public AssessmentHeader.Status getStatus() {
        return status;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    public void setStatus(AssessmentHeader.Status status) {
        this.status = status;
    }

    public enum Status {

        EMPTY("EMPTY"),
        STARTED("STARTED"),
        COMPLETE("COMPLETE");
        private final String value;
        private final static Map<String, AssessmentHeader.Status> CONSTANTS = new HashMap<String, AssessmentHeader.Status>();

        static {
            for (AssessmentHeader.Status c: values()) {
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
        public static AssessmentHeader.Status fromValue(String value) {
            AssessmentHeader.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
