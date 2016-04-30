package org.jtwig.translate.message.source.cache.callable;

import org.jtwig.translate.message.source.MessageSource;

import java.util.Locale;

public class MessageProviderFactory {
    public MessageProvider create(Locale locale, String message, MessageSource messageSource) {
        return new MessageProvider(locale, message, messageSource);
    }
}
