package org.jtwig.translate.message.source.localized.provider;

import org.jtwig.environment.Environment;
import org.jtwig.resource.reference.ResourceReference;

import java.util.ArrayList;
import java.util.Collection;

public class CompositeLocalizedResourceProvider implements LocalizedResourceProvider {
    private final Collection<LocalizedResourceProvider> providers;

    public CompositeLocalizedResourceProvider(Collection<LocalizedResourceProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Collection<ResourceReference> retrieve(Environment environment) {
        Collection<ResourceReference> result = new ArrayList<ResourceReference>();
        for (LocalizedResourceProvider provider : providers) {
            result.addAll(provider.retrieve(environment));
        }
        return result;
    }
}
