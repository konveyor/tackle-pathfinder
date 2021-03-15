package io.tackle.pathfinder.controllers.impl;

import io.tackle.pathfinder.controllers.AssessmentsResource;
import io.tackle.pathfinder.dto.ApplicationDto;
import io.tackle.pathfinder.dto.AssessmentHeaderDto;
import io.tackle.pathfinder.services.AssessmentSvc;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;

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
  public AssessmentHeaderDto createAssessment(ApplicationDto data) {
    return service.createAssessment(data.getApplicationId());
  }

  
}
