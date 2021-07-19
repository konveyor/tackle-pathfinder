package io.tackle.pathfinder.dto;

import io.tackle.pathfinder.model.Risk;
import lombok.Value;

@Value
public class LandscapeDto {
    Long assessmentId;
    Risk risk;
    Long applicationId;
}
