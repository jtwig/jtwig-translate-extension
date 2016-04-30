package org.jtwig.translate.message.source.cache.model;

import java.util.Locale;

public class LocaleAndMessageFactory {
    public LocaleAndMessage create (Locale locale, String message) {
        return new LocaleAndMessage(locale, message);
    }
}
