package io.tackle.pathfinder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bulkId",
    "fromAssessmentId",
    "applications"
})
@Data
@Builder
@AllArgsConstructor()
@NoArgsConstructor
@RegisterForReflection
public class AssessmentBulkDto {
    Long bulkId;
    Long fromAssessmentId;
    Boolean completed;
    List<Long> applications;
}
