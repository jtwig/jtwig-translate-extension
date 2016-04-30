package org.jtwig.translate.message.source.cache;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.MessageSource;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessage;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessageFactory;

import java.util.Locale;
import java.util.concurrent.ConcurrentMap;

public class PersistentMessageSourceCache implements MessageSourceCache {
    private final LocaleAndMessageFactory localeAndMessageFactory;
    private final ConcurrentMap<LocaleAndMessage, Optional<String>> cache;

    public PersistentMessageSourceCache(LocaleAndMessageFactory localeAndMessageFactory, ConcurrentMap<LocaleAndMessage, Optional<String>> cache) {
        this.localeAndMessageFactory = localeAndMessageFactory;
        this.cache = cache;
    }

    @Override
    public Optional<String> retrieve(Locale locale, String message, MessageSource messageSource) {
        LocaleAndMessage localeAndMessage = localeAndMessageFactory.create(locale, message);
        Optional<String> optional = cache.get(localeAndMessage);
        if (optional != null) {
            return optional;
        } else {
            Optional<String> result = messageSource.message(locale, message);
            cache.putIfAbsent(localeAndMessage, result);
            return result;
        }
    }

}
