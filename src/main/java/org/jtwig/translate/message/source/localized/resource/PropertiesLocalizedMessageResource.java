package org.jtwig.translate.message.source.localized.resource;

import com.google.common.base.Optional;

import java.util.Locale;
import java.util.Properties;

public class PropertiesLocalizedMessageResource implements LocalizedMessageResource {
    private final Locale locale;
    private final Properties properties;

    public PropertiesLocalizedMessageResource(Locale locale, Properties properties) {
        this.locale = locale;
        this.properties = properties;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public Optional<String> resolve(String message) {
        return Optional.fromNullable(properties.getProperty(message));
    }
}
