package org.jtwig.translate.message.source.localized.provider;

import org.jtwig.environment.Environment;
import org.jtwig.resource.reference.ResourceReference;

import java.util.Collection;

public interface LocalizedResourceProvider {
    Collection<ResourceReference> retrieve(Environment environment);
}
