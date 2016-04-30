package org.jtwig.translate.message.source.localized.resource;

import com.google.common.base.Optional;

import java.util.Locale;

public interface LocalizedMessageResource {
    Locale getLocale();
    Optional<String> resolve (String message);
}
