package org.jtwig.translate.message.source;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;

import java.util.Locale;
import java.util.Map;

public class InMemoryMessageSource implements MessageSource {
    private final Map<Locale, LocalizedMessageResource> sources;

    public InMemoryMessageSource(Map<Locale, LocalizedMessageResource> sources) {
        this.sources = sources;
    }

    @Override
    public Optional<String> message(Locale locale, String message) {
        LocalizedMessageResource messageResource = sources.get(locale);
        if (messageResource != null) {
            return messageResource.resolve(message);
        }
        return Optional.absent();
    }
}
