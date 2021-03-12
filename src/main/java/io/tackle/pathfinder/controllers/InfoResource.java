package io.tackle.pathfinder.controllers;


import io.tackle.pathfinder.dto.InfoDto;

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
  InfoDto getinfo();
}
