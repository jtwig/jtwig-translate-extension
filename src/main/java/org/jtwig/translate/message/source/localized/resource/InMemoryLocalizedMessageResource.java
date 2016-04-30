package org.jtwig.translate.message.source.localized.resource;

import com.google.common.base.Optional;

import java.util.Locale;
import java.util.Map;

public class InMemoryLocalizedMessageResource implements LocalizedMessageResource {
    private final Locale locale;
    private final Map<String, String> definitions;

    public InMemoryLocalizedMessageResource(Locale locale, Map<String, String> definitions) {
        this.locale = locale;
        this.definitions = definitions;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public Optional<String> resolve(String message) {
        return Optional.fromNullable(definitions.get(message));
    }
}
