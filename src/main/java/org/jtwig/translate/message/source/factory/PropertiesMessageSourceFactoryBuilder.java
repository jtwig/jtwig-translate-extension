package org.jtwig.translate.message.source.factory;

import org.apache.commons.lang3.builder.Builder;
import org.jtwig.translate.message.source.localized.AggregatedLocalizedMessageSourceFactory;
import org.jtwig.translate.message.source.localized.loader.PropertiesLocalizedMessageResourceLoader;
import org.jtwig.translate.message.source.localized.provider.ClasspathLocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.provider.CompositeLocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.provider.FileLocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.provider.LocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.resource.locale.FilenameLocaleExtractor;
import org.jtwig.translate.message.source.localized.resource.locale.RetrieveLocaleExpressionFromFilename;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import static org.jtwig.translate.message.source.localized.provider.file.DirectoryFileFinder.directory;
import static org.jtwig.translate.message.source.localized.provider.file.RecursiveFileFinder.recursiveDirectory;
import static org.jtwig.translate.message.source.localized.provider.io.PropertiesFileFilter.properties;

public class PropertiesMessageSourceFactoryBuilder implements Builder<MessageSourceFactory> {
    public static PropertiesMessageSourceFactoryBuilder propertiesMessageSource () {
        return new PropertiesMessageSourceFactoryBuilder();
    }

    private final Collection<LocalizedResourceProvider> localizedResourceProviders = new ArrayList<>();

    public PropertiesMessageSourceFactoryBuilder withLookupDirectory (String directory) {
        return withLookupDirectory(new File(directory));
    }

    public PropertiesMessageSourceFactoryBuilder withLookupDirectory (File directory) {
        localizedResourceProviders.add(new FileLocalizedResourceProvider(directory, properties(), directory()));
        return this;
    }
    public PropertiesMessageSourceFactoryBuilder withLookupDirectoryRecursively (String directory) {
        return withLookupDirectoryRecursively(new File(directory));
    }

    public PropertiesMessageSourceFactoryBuilder withLookupDirectoryRecursively (File directory) {
        localizedResourceProviders.add(new FileLocalizedResourceProvider(directory, properties(), recursiveDirectory()));
        return this;
    }

    public PropertiesMessageSourceFactoryBuilder withLookupClasspath (String basePackage) {
        localizedResourceProviders.add(new ClasspathLocalizedResourceProvider(getClass().getClassLoader(), basePackage, properties(), directory()));
        return this;
    }

    public PropertiesMessageSourceFactoryBuilder withLookupClasspathRecursively (String basePackage) {
        localizedResourceProviders.add(new ClasspathLocalizedResourceProvider(getClass().getClassLoader(), basePackage, properties(), recursiveDirectory()));
        return this;
    }

    @Override
    public MessageSourceFactory build() {
        return new AggregatedLocalizedMessageSourceFactory(
                new CompositeLocalizedResourceProvider(localizedResourceProviders),
                new PropertiesLocalizedMessageResourceLoader(new FilenameLocaleExtractor(RetrieveLocaleExpressionFromFilename.instance()))
        );
    }
}
