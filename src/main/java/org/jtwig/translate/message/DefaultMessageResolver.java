package org.jtwig.translate.message;

import com.google.common.base.Optional;
import org.jtwig.translate.message.decorate.MessageDecorator;
import org.jtwig.translate.message.source.MessageSource;

import java.util.Locale;

public class DefaultMessageResolver implements MessageResolver {
    private final MessageSource messageSource;

    public DefaultMessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Optional<String> resolve(Locale locale, String message, MessageDecorator decorator) {
        String input = (message == null) ? null : message.trim();
        Optional<String> result = messageSource.message(locale, input);
        if (result.isPresent()) {
            return Optional.fromNullable(decorator.decorate(result.get()));
        }
        return Optional.absent();
    }
}
