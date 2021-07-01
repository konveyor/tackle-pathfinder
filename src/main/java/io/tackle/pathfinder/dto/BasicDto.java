package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class BasicDto {
    /**
     * DB id
     * (Required)
     *
     */
    @JsonProperty("id")
    @JsonPropertyDescription("DB id")
    private Long id;

    public BasicDto() {
    }
}
