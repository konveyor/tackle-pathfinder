package io.tackle.pathfinder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "applications",
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
    @JsonProperty("applications")
    @JsonPropertyDescription("")
    private List<Integer> applications = new ArrayList<Integer>();
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
    @JsonProperty("applications")
    public List<Integer> getApplications() {
        return applications;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("applications")
    public void setApplications(List<Integer> applications) {
        this.applications = applications;
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
