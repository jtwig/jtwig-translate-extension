package org.jtwig.translate.message.source.localized.loader;

import org.jtwig.environment.Environment;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;

public interface LocalizedMessageResourceLoader {
    LocalizedMessageResource load (Environment environment, ResourceReference resourceReference);
}
