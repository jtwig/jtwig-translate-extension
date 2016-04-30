package org.jtwig.translate.message.source.cache;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.MessageSource;

import java.util.Locale;

public class CachedMessageSource implements MessageSource {
    private final MessageSourceCache cache;
    private final MessageSource messageSource;

    public CachedMessageSource(MessageSourceCache cache, MessageSource messageSource) {
        this.cache = cache;
        this.messageSource = messageSource;
    }

    @Override
    public Optional<String> message(Locale locale, String message) {
        return cache.retrieve(locale, message, messageSource);
    }
}
