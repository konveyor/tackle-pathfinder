package io.tackle.pathfinder.translation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.mapper.AssessmentMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.io.IOException;

@ApplicationScoped
public class TranslateSerializer extends JsonSerializer<AssessmentDto> {
    @Override
    @Transactional
    public void serialize(AssessmentDto s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String language = s.getQuestionnaire().getLanguage();

        jsonGenerator.writeString("AA" + s);
    }

    public AssessmentDto translate(AssessmentDto assessmentDto) {
        assessmentDto.getQuestionnaire().setTitle(translate(assessmentDto.getQuestionnaire().getTitle(), assessmentDto.getQuestionnaire().getLanguage()));
        assessmentDto.getQuestionnaire().getCategories().stream()
            .forEach(a -> {
                a.setTitle(translate(a.getTitle(), assessmentDto.getQuestionnaire().getLanguage()));
                a.getQuestions().stream().forEach(b -> {
                    b.setDescription(translate(b.getDescription(), assessmentDto.getQuestionnaire().getLanguage()));
                    b.setQuestion(translate(b.getQuestion(), assessmentDto.getQuestionnaire().getLanguage()));
                    b.getOptions().stream().forEach(c -> c.setOption(translate(c.getOption(), assessmentDto.getQuestionnaire().getLanguage())));
                });
            });

        return assessmentDto;
    }

    private String translate(String title, String language) {
        return "x";
    }
}
