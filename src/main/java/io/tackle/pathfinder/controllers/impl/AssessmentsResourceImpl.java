package io.tackle.pathfinder.controllers.impl;

import io.tackle.pathfinder.controllers.AssessmentsResource;
import io.tackle.pathfinder.dto.ApplicationDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.services.AssessmentSvc;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;
import java.util.stream.Collectors;

public class AssessmentsResourceImpl implements AssessmentsResource {
  @Inject
  AssessmentSvc service;

  @Override
  public List<AssessmentHeaderDto> getApplicationAssessments(@QueryParam("applicationId") Long applicationId) {
    return service.getAssessmentHeaderDtoByApplicationId(applicationId).stream().collect(Collectors.toList());
  }

  @Override
  public Response createAssessment(ApplicationDto data) {
    return Response
      .status(Status.CREATED)
      .entity(service.createAssessment(data.getApplicationId()))
      .type(MediaType.APPLICATION_JSON)
      .build();
  }

  
}
