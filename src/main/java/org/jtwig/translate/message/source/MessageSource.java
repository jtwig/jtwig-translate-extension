package org.jtwig.translate.message.source;

import com.google.common.base.Optional;

import java.util.Locale;

public interface MessageSource {
    Optional<String> message(Locale locale, String message);
}
