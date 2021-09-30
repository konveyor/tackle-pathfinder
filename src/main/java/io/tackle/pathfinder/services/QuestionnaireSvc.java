package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.questionnaire.QuestionnaireHeaderDto;
import io.tackle.pathfinder.mapper.QuestionnaireMapper;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class QuestionnaireSvc {
    @Inject
    QuestionnaireMapper questionnaireMapper;

    public List<QuestionnaireHeaderDto> getQuestionnaireHeaderList(String lang) {
        return Questionnaire.listAll().stream()
            .map(a -> questionnaireMapper.questionnaireToQuestionnaireHeaderDto((Questionnaire) a, lang))
            .collect(Collectors.toList());
    }
}
