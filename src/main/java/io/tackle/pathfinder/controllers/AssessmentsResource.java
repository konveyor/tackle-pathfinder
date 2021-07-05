package io.tackle.pathfinder.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.tackle.pathfinder.dto.*;
import io.tackle.pathfinder.services.AssessmentSvc;
import io.tackle.pathfinder.services.TranslatorSvc;
import lombok.extern.java.Log;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.stream.Collectors;

@Path("/assessments")
@Log
public class AssessmentsResource {
  @Inject
  AssessmentSvc assessmentSvc;

  @Inject
  TranslatorSvc translatorSvc;

  @Inject
  JsonWebToken accessToken;

  @GET
  @Produces("application/json")
  public List<AssessmentHeaderDto> getApplicationAssessments(@NotNull @QueryParam("applicationId") Long applicationId) {
    return assessmentSvc.getAssessmentHeaderDtoByApplicationId(applicationId).stream().collect(Collectors.toList());
  }

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  public Response createAssessment(@QueryParam("fromAssessmentId") Long fromAssessmentId, @NotNull @Valid ApplicationDto data) {
    AssessmentHeaderDto createAssessment;
    
    if (fromAssessmentId != null) {
      createAssessment = assessmentSvc.copyAssessment(fromAssessmentId, data.getApplicationId());
    } else {
      createAssessment = assessmentSvc.createAssessment(data.getApplicationId());
    }
    
    return Response
      .status(Status.CREATED)
      .entity(createAssessment)
      .type(MediaType.APPLICATION_JSON)
      .build();
  }

  @GET
  @Path("{assessmentId}")
  @Produces("application/json")
  public AssessmentDto getAssessment(@NotNull @PathParam("assessmentId") Long assessmentId, @QueryParam("language") String language) throws JsonProcessingException {
    String lang = translatorSvc.getLanguage(accessToken.getRawToken(), language);
    return assessmentSvc.getAssessmentDtoByAssessmentId(assessmentId, lang);
  }  
  
  @PATCH
  @Path("{assessmentId}")
  @Produces("application/json")
  @Consumes("application/json")
  public AssessmentHeaderDto updateAssessment(@NotNull @PathParam("assessmentId") Long assessmentId, @NotNull @Valid AssessmentDto assessment) {
    return assessmentSvc.updateAssessment(assessmentId, assessment);
  }

  @DELETE
  @Path("{assessmentId}")
  @Produces("application/json")
  public Response deleteAssessment(@NotNull @PathParam("assessmentId") Long assessmentId) {
    assessmentSvc.deleteAssessment(assessmentId);
    return Response.ok().status(Response.Status.NO_CONTENT).build();
  }

  @POST
  @Path("risks")
  @Produces("application/json")
  @Consumes("application/json")
  public List<RiskLineDto> getIdentifiedRisks(@NotNull @Valid List<ApplicationDto> applicationList, @QueryParam("language") String language) {
    if (!applicationList.isEmpty()) {
      // TODO Tanslate
      return assessmentSvc.identifiedRisks(applicationList.stream().map(e -> e.getApplicationId()).collect(Collectors.toList()));
    } else {
      throw new BadRequestException();
    }
  }

  @POST
  @Path("/assessment-risk")
  @Produces("application/json")
  @Consumes("application/json")
  public List<LandscapeDto> getLandscape(@NotNull @Valid List<ApplicationDto> applicationIds) {
    if (applicationIds.isEmpty()) throw new BadRequestException();
    return assessmentSvc.landscape(applicationIds.stream().map(e -> e.getApplicationId()).collect(Collectors.toList()));
  }

  @POST
  @Path("/confidence")
  @Produces("application/json")
  @Consumes("application/json")
  public List<AdoptionCandidateDto> adoptionCandidate(@NotNull @Valid List<ApplicationDto> applicationId) {
    return assessmentSvc.getAdoptionCandidate(applicationId.stream().map(a -> a.getApplicationId()).collect(Collectors.toList()));
  }

}
