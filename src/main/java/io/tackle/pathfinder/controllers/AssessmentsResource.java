package io.tackle.pathfinder.controllers;

import io.tackle.pathfinder.dto.ApplicationDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.util.List;

/**
 * A JAX-RS interface.  An implementation of this interface must be provided.
 */
@Path("/assessments")
public interface AssessmentsResource {
  /**
   * Gets the details of a single instance of a `assessment`.
   */
  @GET
  @Produces("application/json")
  public List<AssessmentHeaderDto> getApplicationAssessments(@NotNull @QueryParam("applicationId") Long applicationId);

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  Response createAssessment(@NotNull ApplicationDto data);
}
