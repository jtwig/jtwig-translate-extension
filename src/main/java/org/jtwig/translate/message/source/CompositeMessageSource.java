package org.jtwig.translate.message.source;

import com.google.common.base.Optional;

import java.util.Collection;
import java.util.Locale;

public class CompositeMessageSource implements MessageSource {
    private final Collection<MessageSource> messageSources;

    public CompositeMessageSource(Collection<MessageSource> messageSources) {
        this.messageSources = messageSources;
    }

    @Override
    public Optional<String> message(Locale locale, String message) {
        for (MessageSource messageSource : messageSources) {
            Optional<String> result = messageSource.message(locale, message);
            if (result.isPresent()) return result;
        }
        return Optional.absent();
    }
}
