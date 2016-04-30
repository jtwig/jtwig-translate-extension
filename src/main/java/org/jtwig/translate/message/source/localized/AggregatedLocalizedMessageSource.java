package org.jtwig.translate.message.source.localized;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.MessageSource;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class AggregatedLocalizedMessageSource implements MessageSource {
    private final Map<Locale, Collection<LocalizedMessageResource>> localizedMessageResources;

    public AggregatedLocalizedMessageSource(Map<Locale, Collection<LocalizedMessageResource>> localizedMessageResources) {
        this.localizedMessageResources = localizedMessageResources;
    }

    @Override
    public Optional<String> message(Locale locale, String message) {
        Optional<Collection<LocalizedMessageResource>> resources = Optional.fromNullable(this.localizedMessageResources.get(locale));
        if (resources.isPresent()) {
            for (LocalizedMessageResource resource : resources.get()) {
                Optional<String> resolve = resource.resolve(message);
                if (resolve.isPresent()) return resolve;
            }
        }
        return Optional.absent();
    }
}
