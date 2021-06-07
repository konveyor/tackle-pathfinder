package io.tackle.pathfinder.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkOperation {
    String username;
    Long bulkId;
}
