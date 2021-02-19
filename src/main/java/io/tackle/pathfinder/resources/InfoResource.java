package io.tackle.pathfinder.resources;


import io.tackle.pathfinder.model.Info;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * A JAX-RS interface.  An implementation of this interface must be provided.
 */
@Path("/info")
public interface InfoResource {
  /**
   * Gets a list of all `info` entities.
   */
  @GET
  @Produces("application/json")
  Info getinfo();
}
