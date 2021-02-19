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
 * Root Type for questionaire
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "id",
    "language",
    "categories",
    "description"
})
public class Questionaire {

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
    @JsonProperty("id")
    @JsonPropertyDescription("")
    private Integer id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("language")
    @JsonPropertyDescription("")
    private Questionaire.Language language;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("categories")
    @JsonPropertyDescription("")
    private List<Category> categories = new ArrayList<Category>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("description")
    @JsonPropertyDescription("")
    private String description;

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
    @JsonProperty("language")
    public Questionaire.Language getLanguage() {
        return language;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("language")
    public void setLanguage(Questionaire.Language language) {
        this.language = language;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public enum Language {

        ES("ES"),
        EN("EN"),
        FR("FR"),
        IT("IT");
        private final String value;
        private final static Map<String, Questionaire.Language> CONSTANTS = new HashMap<String, Questionaire.Language>();

        static {
            for (Questionaire.Language c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Language(String value) {
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
        public static Questionaire.Language fromValue(String value) {
            Questionaire.Language constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
