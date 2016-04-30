package org.jtwig.translate.message.source.cache.callable;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.MessageSource;

import java.util.Locale;
import java.util.concurrent.Callable;

public class MessageProvider implements Callable<Optional<String>> {
    private final Locale locale;
    private final String message;
    private final MessageSource messageSource;

    public MessageProvider(Locale locale, String message, MessageSource messageSource) {
        this.locale = locale;
        this.message = message;
        this.messageSource = messageSource;
    }

    @Override
    public Optional<String> call() throws Exception {
        return messageSource.message(locale, message);
    }
}
