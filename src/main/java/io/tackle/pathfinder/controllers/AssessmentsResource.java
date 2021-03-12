package io.tackle.pathfinder.controllers;

import io.tackle.pathfinder.dto.AssessmentHeaderDto;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * A JAX-RS interface.  An implementation of this interface must be provided.
 */
@Path("/applications/{applicationId}/assessments")
public interface AssessmentsResource {
  /**
   * Gets the details of a single instance of a `assessment`.
   */
  @GET
  @Produces("application/json")
  public AssessmentHeaderDto getApplicationAssessments(@PathParam("applicationId") Long applicationId);
}
