package org.jtwig.translate.message.source.localized;

import org.jtwig.environment.Environment;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.translate.message.source.factory.MessageSourceFactory;
import org.jtwig.translate.message.source.localized.loader.LocalizedMessageResourceLoader;
import org.jtwig.translate.message.source.localized.provider.LocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;

import java.util.*;

public class AggregatedLocalizedMessageSourceFactory implements MessageSourceFactory {
    private final LocalizedResourceProvider localizedResourceProvider;
    private final LocalizedMessageResourceLoader localizedMessageResourceLoader;

    public AggregatedLocalizedMessageSourceFactory(LocalizedResourceProvider localizedResourceProvider, LocalizedMessageResourceLoader localizedMessageResourceLoader) {
        this.localizedResourceProvider = localizedResourceProvider;
        this.localizedMessageResourceLoader = localizedMessageResourceLoader;
    }

    @Override
    public AggregatedLocalizedMessageSource create (Environment environment) {
        Collection<ResourceReference> resourceReferences = localizedResourceProvider.retrieve(environment);
        Map<Locale, Collection<LocalizedMessageResource>> collectionMap = new HashMap<Locale, Collection<LocalizedMessageResource>>();

        for (ResourceReference resourceReference : resourceReferences) {
            LocalizedMessageResource localizedMessageResource = localizedMessageResourceLoader.load(environment, resourceReference);
            if (!collectionMap.containsKey(localizedMessageResource.getLocale())) {
                collectionMap.put(localizedMessageResource.getLocale(), new ArrayList<LocalizedMessageResource>());
            }

            collectionMap.get(localizedMessageResource.getLocale())
                    .add(localizedMessageResource);
        }

        return new AggregatedLocalizedMessageSource(collectionMap);
    }
}
