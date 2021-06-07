package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "applicationId",
    "id",
    "error"
})
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor()
@NoArgsConstructor
@RegisterForReflection
public class AssessmentHeaderBulkDto extends AssessmentHeaderDto {
    @JsonProperty("error")
    @JsonPropertyDescription("")
    private String error;
}
