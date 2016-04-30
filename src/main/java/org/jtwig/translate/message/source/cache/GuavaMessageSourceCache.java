package org.jtwig.translate.message.source.cache;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import org.jtwig.translate.message.source.MessageSource;
import org.jtwig.translate.message.source.cache.callable.MessageProvider;
import org.jtwig.translate.message.source.cache.callable.MessageProviderFactory;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessage;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessageFactory;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class GuavaMessageSourceCache implements MessageSourceCache {
    private final LocaleAndMessageFactory localeAndMessageFactory;
    private final MessageProviderFactory messageProviderFactory;
    private final Cache<LocaleAndMessage, Optional<String>> cache;

    public GuavaMessageSourceCache(LocaleAndMessageFactory localeAndMessageFactory, MessageProviderFactory messageProviderFactory, Cache<LocaleAndMessage, Optional<String>> cache) {
        this.localeAndMessageFactory = localeAndMessageFactory;
        this.messageProviderFactory = messageProviderFactory;
        this.cache = cache;
    }

    @Override
    public Optional<String> retrieve(Locale locale, String message, MessageSource messageSource) {
        LocaleAndMessage localeAndMessage = localeAndMessageFactory.create(locale, message);
        try {
            MessageProvider messageProvider = messageProviderFactory.create(locale, message, messageSource);
            return cache.get(localeAndMessage, messageProvider);
        } catch (ExecutionException e) {
            throw new IllegalArgumentException("Unable to retrieve message", e);
        }
    }

}
