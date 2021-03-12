package io.tackle.pathfinder.controllers.impl;

import io.tackle.pathfinder.controllers.AssessmentsResource;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.services.AssessmentSvc;

import javax.inject.Inject;
import javax.ws.rs.PathParam;

public class AssessmentsResourceImpl implements AssessmentsResource {
  @Inject
  AssessmentSvc service;

  @Override
  public AssessmentHeaderDto getApplicationAssessments(@PathParam("applicationId") Long applicationId) {
    return service.gAssessmentHeaderDtoByApplicationId(applicationId);
  }
}
