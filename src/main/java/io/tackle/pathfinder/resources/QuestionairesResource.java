package io.tackle.pathfinder.resources;

import io.tackle.pathfinder.model.Questionaire;
import io.tackle.pathfinder.model.QuestionaireHeader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * A JAX-RS interface.  An implementation of this interface must be provided.
 */
@Path("/questionaires")
public interface QuestionairesResource {
  @Path("/{questionaireId}")
  @GET
  @Produces("application/json")
  Questionaire generatedMethod1(@PathParam("questionaireId") Integer questionaireId);

  @Path("/{questionaireId}/import")
  @PUT
  @Consumes("application/json")
  void generatedMethod2(@PathParam("questionaireId") Integer questionaireId,
      @QueryParam("oldformat") Boolean oldformat, Questionaire data);

  @Path("/{questionaireId}/export")
  @PUT
  @Consumes("application/json")
  void generatedMethod3(@PathParam("questionaireId") Integer questionaireId, Questionaire data);

  @GET
  @Produces("application/json")
  QuestionaireHeader generatedMethod4();
}
