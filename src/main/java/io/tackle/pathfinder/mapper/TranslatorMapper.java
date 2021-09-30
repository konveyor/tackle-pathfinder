package io.tackle.pathfinder.mapper;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.services.TranslatorSvc;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public abstract class TranslatorMapper {
    @Inject
    TranslatorSvc translatorSvc;

    protected String translate(PanacheEntity dto, String defaultText, String destLanguage, String field) {
        if (StringUtils.isBlank(destLanguage)) return defaultText;

        String key = translatorSvc.getKey(dto, field);
        return translatorSvc.translate(key, destLanguage, defaultText);
    }

    protected String translate(String table, Long id, String defaultText, String destLanguage, String field) {
        if (StringUtils.isBlank(destLanguage)) return defaultText;

        String key = translatorSvc.getKey(table, id, field);
        return translatorSvc.translate(key, destLanguage, defaultText);
    }
}
