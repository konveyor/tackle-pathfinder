package io.tackle.pathfinder.controllers;

import io.tackle.pathfinder.dto.ApplicationDto;
import io.tackle.pathfinder.dto.AssessmentBulkDto;
import io.tackle.pathfinder.dto.AssessmentBulkPostDto;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.services.AssessmentSvc;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;
import java.util.stream.Collectors;

@Path("/assessments")
@Log
public class AssessmentsResource {
  @Inject
  AssessmentSvc service;

  @Inject
  EventBus eventBus;

  @GET
  @Produces("application/json")
  public List<AssessmentHeaderDto> getApplicationAssessments(@NotNull @QueryParam("applicationId") Long applicationId) {
    return service.getAssessmentHeaderDtoByApplicationId(applicationId).stream().collect(Collectors.toList());
  }

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  public Response createAssessment(@QueryParam("fromAssessmentId") Long fromAssessmentId, @NotNull @Valid ApplicationDto data) {

    AssessmentHeaderDto createAssessment = service.newAssessment(fromAssessmentId, data.getApplicationId());

    return Response
      .status(Status.CREATED)
      .entity(createAssessment)
      .type(MediaType.APPLICATION_JSON)
      .build();
  }

  @GET
  @Path("{assessmentId}")
  @Produces("application/json")
  public AssessmentDto getAssessment(@NotNull @PathParam("assessmentId") Long assessmentId) {
    return service.getAssessmentDtoByAssessmentId(assessmentId);
  }  
  
  @PATCH
  @Path("{assessmentId}")
  @Produces("application/json")
  @Consumes("application/json")
  public AssessmentHeaderDto updateAssessment(@NotNull @PathParam("assessmentId") Long assessmentId, @NotNull @Valid AssessmentDto assessment) {
    return service.updateAssessment(assessmentId, assessment);
  }

  @DELETE
  @Path("{assessmentId}")
  @Produces("application/json")
  public Response deleteAssessment(@NotNull @PathParam("assessmentId") Long assessmentId) {
    service.deleteAssessment(assessmentId);
    return Response.ok().status(Response.Status.NO_CONTENT).build();
  }

  @POST
  @Path("bulk")
  @Produces("application/json")
  @Consumes("application/json")
  public AssessmentBulkDto bulkCreate(@NotNull @Valid AssessmentBulkPostDto data) {
    log.info("gets to inside method");
    List<Long> appsList = data.getApplications().stream()
                          .map(e -> e.getApplicationId())
                          .collect(Collectors.toList());
    log.info("gets after list");
    return service.bulkCreateAssessments(data.getFromAssessmentId(), appsList);
  }

  @GET
  @Path("bulk/{bulkId}")
  @Produces("application/json")
  @Consumes("application/json")
  public AssessmentBulkDto bulkGet(@NotNull @PathParam("bulkId") Long bulkId) {
    return service.bulkGet(bulkId);
  }
}
