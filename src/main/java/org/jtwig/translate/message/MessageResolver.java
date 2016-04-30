package org.jtwig.translate.message;

import com.google.common.base.Optional;
import org.jtwig.translate.message.decorate.MessageDecorator;

import java.util.Locale;

public interface MessageResolver {
    Optional<String> resolve(Locale locale, String message, MessageDecorator decorator);
}
