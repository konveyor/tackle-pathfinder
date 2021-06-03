package io.tackle.pathfinder.translation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.tackle.pathfinder.dto.AssessmentDto;

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
}
