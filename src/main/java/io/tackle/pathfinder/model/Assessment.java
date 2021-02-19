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
    "date",
    "user",
    "status",
    "categories"
})
public class Assessment {

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
    private Assessment.Status status;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("categories")
    @JsonPropertyDescription("")
    private List<AssessCategory> categories = new ArrayList<AssessCategory>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    public Assessment.Status getStatus() {
        return status;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    public void setStatus(Assessment.Status status) {
        this.status = status;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("categories")
    public List<AssessCategory> getCategories() {
        return categories;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("categories")
    public void setCategories(List<AssessCategory> categories) {
        this.categories = categories;
    }

    public enum Status {

        EMPTY("EMPTY"),
        STARTED("STARTED"),
        COMPLETE("COMPLETE"),
        DISCARDED("DISCARDED");
        private final String value;
        private final static Map<String, Assessment.Status> CONSTANTS = new HashMap<String, Assessment.Status>();

        static {
            for (Assessment.Status c: values()) {
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
        public static Assessment.Status fromValue(String value) {
            Assessment.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
