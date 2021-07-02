package io.tackle.pathfinder.services;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.model.i18n.TranslatedText;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

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
        return TranslatedText.find("key=?1 and language=?2", key, destinationLanguage)
            .firstResultOptional().map( a -> ((TranslatedText)a).text)
            .orElse(defaultText);
    }

    public TranslatedText addOrUpdateTranslation(@NotNull PanacheEntity entity, String concept, String text, String language) {
        return addOrUpdateTranslation(getKey(entity, concept), text, language);
    }

    @Transactional
    public TranslatedText addOrUpdateTranslation(@NotNull String key, String text, String language) {
        TranslatedText translatedText = (TranslatedText) TranslatedText.find("key=?1 and language=?2", key, language)
            .firstResultOptional()
            .map(a -> {
                ((TranslatedText)a).text = text;
                return a;
            })
            .orElseGet(() -> TranslatedText.builder()
                .key(key)
                .text(text)
                .language(language).build());
        translatedText.persist();
        return translatedText;
    }
}
