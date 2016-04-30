package org.jtwig.translate.message.source.localized.loader;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.resource.ResourceService;
import org.jtwig.resource.exceptions.ResourceException;
import org.jtwig.resource.exceptions.ResourceNotFoundException;
import org.jtwig.resource.metadata.ResourceMetadata;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;
import org.jtwig.translate.message.source.localized.resource.PropertiesLocalizedMessageResource;
import org.jtwig.translate.message.source.localized.resource.locale.FilenameLocaleExtractor;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class PropertiesLocalizedMessageResourceLoader implements LocalizedMessageResourceLoader {
    private final FilenameLocaleExtractor localeExtractor;

    public PropertiesLocalizedMessageResourceLoader(FilenameLocaleExtractor localeExtractor) {
        this.localeExtractor = localeExtractor;
    }

    @Override
    public LocalizedMessageResource load(Environment environment, ResourceReference resourceReference) {
        ResourceService resourceService = environment.getResourceEnvironment().getResourceService();
        ResourceMetadata resourceMetadata = resourceService.loadMetadata(resourceReference);
        if (!resourceMetadata.exists()) {
            throw new ResourceNotFoundException(String.format("Resource '%s' not found", resourceReference));
        } else {
            Properties properties = new Properties();
            try {
                properties.load(resourceMetadata.load());
                Optional<Locale> localeOptional = localeExtractor.extractLocale(resourceMetadata);
                if (!localeOptional.isPresent()) {
                    throw new ResourceException(String.format("Could not extract locale from resource '%s'", resourceReference));
                } else {
                    return new PropertiesLocalizedMessageResource(localeOptional.get(), properties);
                }
            } catch (IOException e) {
                throw new ResourceException(String.format("Cannot load properties file '%s'", resourceReference), e);
            }
        }
    }
}
