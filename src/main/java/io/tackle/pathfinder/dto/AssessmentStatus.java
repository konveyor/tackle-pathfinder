package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum AssessmentStatus {

    EMPTY("EMPTY"),
    STARTED("STARTED"),
    COMPLETE("COMPLETE"),
    DISCARDED("DISCARDED");
    private final String value;
    private static final Map<String, AssessmentStatus> CONSTANTS = new HashMap<>();

    static {
        for (AssessmentStatus c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private AssessmentStatus(String value) {
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
    public static AssessmentStatus fromValue(String value) {
        AssessmentStatus constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}