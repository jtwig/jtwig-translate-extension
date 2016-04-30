package org.jtwig.translate.message.source.cache;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.MessageSource;

import java.util.Locale;

public interface MessageSourceCache {
    Optional<String> retrieve(Locale locale, String message, MessageSource messageSource);
}
