package io.tackle.pathfinder.services;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.model.i18n.TranslatedText;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApplicationScoped
@Log
public class TranslatorSvc {

    public String getKey(PanacheEntity b, String concept) {
        return String.format("%s_%s_%s", b.getClass().getSimpleName(), b.id, concept);
    }

    @Transactional
    public String translate(@NotNull String key, @NotNull String destinationLanguage, String defaultText) {
        if (StringUtils.isBlank(destinationLanguage)) {
            return defaultText;
        }
        TranslatedText translatedTextObj = TranslatedText.find("key=?1 and language=?2", key, destinationLanguage).firstResult();

        return (translatedTextObj != null) ? translatedTextObj.text : defaultText;
    }

    public void addTranslation(@NotNull PanacheEntity entity, String concept, String text, String language) {
        addTranslation(getKey(entity, concept), text, language);
    }

    @Transactional
    public void addTranslation(@NotNull String key, String text, String language) {
        TranslatedText.builder()
            .key(key)
            .text(text)
            .language(language).build().persistAndFlush();
    }
}
