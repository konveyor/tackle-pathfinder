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
    "completed",
    "applications",
    "assessments"
})
@Data
@Builder
@AllArgsConstructor()
@NoArgsConstructor
@RegisterForReflection
public class AssessmentBulkDto {
    private Long bulkId;
    private Long fromAssessmentId;
    private Boolean completed;
    private List<Long> applications;

    private List<AssessmentHeaderBulkDto> assessments;
}
