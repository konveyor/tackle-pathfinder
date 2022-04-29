/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
