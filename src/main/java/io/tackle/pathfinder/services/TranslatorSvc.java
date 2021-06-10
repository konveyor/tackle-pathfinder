package io.tackle.pathfinder.services;

import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.dto.RiskLineDto;
import io.tackle.pathfinder.model.i18n.TranslatedText;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TranslatorSvc {

    public AssessmentDto translate(AssessmentDto assessmentDto, String language) {
        assessmentDto.getQuestionnaire().setTitle(translate(assessmentDto.getQuestionnaire().getTitle(), assessmentDto.getQuestionnaire().getLanguage(), language));
        assessmentDto.getQuestionnaire().getCategories().stream()
            .forEach(a -> {
                a.setTitle(translate(a.getTitle(), assessmentDto.getQuestionnaire().getLanguage(), language));
                a.getQuestions().stream().forEach(b -> {
                    b.setDescription(translate(b.getDescription(), assessmentDto.getQuestionnaire().getLanguage(), language));
                    b.setQuestion(translate(b.getQuestion(), assessmentDto.getQuestionnaire().getLanguage(), language));
                    b.getOptions().stream().forEach(c -> c.setOption(translate(c.getOption(), assessmentDto.getQuestionnaire().getLanguage(), language)));
                });
            });

        return assessmentDto;
    }

    private String translate(String originalText, String originalLanguage, String destinationLanguage) {
        if (destinationLanguage == null) {
            return originalText;
        }

        TranslatedText originalTextObj = TranslatedText.find("text=%1 and language=%2", originalText, originalLanguage).firstResult();
        TranslatedText translatedTextObj = TranslatedText.find("group=%1 and language=%2", originalTextObj.group, destinationLanguage).firstResult();

        return translatedTextObj.text;
    }
}
