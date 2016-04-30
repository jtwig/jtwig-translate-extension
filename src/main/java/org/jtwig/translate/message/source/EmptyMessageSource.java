package org.jtwig.translate.message.source;

import com.google.common.base.Optional;

import java.util.Locale;

public class EmptyMessageSource implements MessageSource {
    public static EmptyMessageSource emptyMessageSource () {
        return new EmptyMessageSource();
    }

    private EmptyMessageSource() {}

    @Override
    public Optional<String> message(Locale locale, String message) {
        return Optional.absent();
    }
}
