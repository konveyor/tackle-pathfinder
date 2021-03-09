package io.tackle.pathfinder.controllers;

import io.tackle.pathfinder.dto.Assessment;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.List;

/**
 * A JAX-RS interface.  An implementation of this interface must be provided.
 */
@Path("/assessments")
public class AssessmentsResource {
  /**
   * Gets the details of a single instance of a `assessment`.
   */
  @Path("/{assessmentId}")
  @GET
  @Produces("application/json")
  public Assessment getassessment(@PathParam("assessmentId") String assessmentId) {
    return new Assessment();
  }

  /**
   * Updates an existing `assessment`.
   */
  @Path("/{assessmentId}")
  @PUT
  @Consumes("application/json")
  public void updateassessment(@PathParam("assessmentId") String assessmentId, Assessment data) {
    // do nothing
  }

  /**
   * Deletes an existing `assessment`.
   */
  @Path("/{assessmentId}")
  @DELETE
  public void deleteassessment(@PathParam("assessmentId") String assessmentId) {
    // do nothing
  }

  /**
   *
   */
  @GET
  public void generatedMethod5(@QueryParam("applications") List<Integer> applications) {
    // do nothing
  }

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  public String generatedMethod6(Assessment data) {
    return "";
  }
}
