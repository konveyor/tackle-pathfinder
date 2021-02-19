package io.tackle.pathfinder.resources;

import io.tackle.pathfinder.model.Assessment;
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
public interface AssessmentsResource {
  /**
   * Gets the details of a single instance of a `assessment`.
   */
  @Path("/{assessmentId}")
  @GET
  @Produces("application/json")
  Assessment getassessment(@PathParam("assessmentId") String assessmentId);

  /**
   * Updates an existing `assessment`.
   */
  @Path("/{assessmentId}")
  @PUT
  @Consumes("application/json")
  void updateassessment(@PathParam("assessmentId") String assessmentId, Assessment data);

  /**
   * Deletes an existing `assessment`.
   */
  @Path("/{assessmentId}")
  @DELETE
  void deleteassessment(@PathParam("assessmentId") String assessmentId);

  /**
   *
   */
  @GET
  void generatedMethod5(@QueryParam("applications") List<Integer> applications);

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  String generatedMethod6(Assessment data);
}
