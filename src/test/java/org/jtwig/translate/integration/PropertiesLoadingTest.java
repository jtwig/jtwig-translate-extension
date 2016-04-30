package org.jtwig.translate.integration;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.resource.exceptions.ResourceException;
import org.jtwig.translate.TranslateExtension;
import org.jtwig.translate.configuration.DefaultTranslateConfiguration;
import org.jtwig.translate.configuration.TranslateConfigurationBuilder;
import org.jtwig.translate.message.source.cache.CachedMessageSourceFactory;
import org.jtwig.translate.message.source.cache.GuavaMessageSourceCache;
import org.jtwig.translate.message.source.cache.PersistentMessageSourceCache;
import org.jtwig.translate.message.source.cache.callable.MessageProviderFactory;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessage;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessageFactory;
import org.jtwig.translate.message.source.localized.AggregatedLocalizedMessageSourceFactory;
import org.jtwig.translate.message.source.localized.loader.PropertiesLocalizedMessageResourceLoader;
import org.jtwig.translate.message.source.localized.provider.ClasspathLocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.provider.CompositeLocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.provider.FileLocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.provider.LocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.provider.file.DirectoryFileFinder;
import org.jtwig.translate.message.source.localized.provider.file.RecursiveFileFinder;
import org.jtwig.translate.message.source.localized.resource.locale.FilenameLocaleExtractor;
import org.jtwig.translate.message.source.localized.resource.locale.RetrieveLocaleExpressionFromFilename;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.jtwig.environment.EnvironmentConfigurationBuilder.configuration;

public class PropertiesLoadingTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNotFound() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{{ 'hello' | translate ('fr') }}",
                configuration()
                        .extensions()
                        .add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(new AggregatedLocalizedMessageSourceFactory(
                                        new FileLocalizedResourceProvider(new File("src/test/resources/translation"), propertiesFile(), new DirectoryFileFinder()),
                                        new PropertiesLocalizedMessageResourceLoader(new FilenameLocaleExtractor(new RetrieveLocaleExpressionFromFilename()))
                                ))
                                .build()))
                        .and()
                        .build()
        ).render(JtwigModel.newModel());

        assertThat(result, is("hello"));
    }

    @Test
    public void test() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{{ 'hello' | translate ('it') }} - {{ 'hello' | translate ('pt') }}",
                configuration()
                        .extensions()
                        .add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(new AggregatedLocalizedMessageSourceFactory(
                                        new FileLocalizedResourceProvider(new File("src/test/resources/translation"), propertiesFile(), new DirectoryFileFinder()),
                                        new PropertiesLocalizedMessageResourceLoader(new FilenameLocaleExtractor(new RetrieveLocaleExpressionFromFilename()))
                                ))
                                .build()))
                        .and()
                        .build()
        ).render(JtwigModel.newModel());

        assertThat(result, is("Ciao - Ola"));
    }

    @Test
    public void testClasspath() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{{ 'hello' | translate ('it') }} - {{ 'hello' | translate ('pt') }}",
                configuration()
                        .extensions()
                        .add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(new AggregatedLocalizedMessageSourceFactory(
                                        new ClasspathLocalizedResourceProvider(getClass().getClassLoader(), "translation", propertiesFile(), new DirectoryFileFinder()),
                                        new PropertiesLocalizedMessageResourceLoader(new FilenameLocaleExtractor(new RetrieveLocaleExpressionFromFilename()))
                                ))
                                .build()))
                        .and()
                        .build()
        ).render(JtwigModel.newModel());

        assertThat(result, is("Ciao - Ola"));
    }

    @Test
    public void testException() throws Exception {
        expectedException.expect(ResourceException.class);
        expectedException.expectMessage("Could not extract locale from resource");

        JtwigTemplate.inlineTemplate("{{ 'hello' | translate ('it') }} - {{ 'hello' | translate ('pt') }}",
                configuration()
                        .extensions()
                        .add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(new AggregatedLocalizedMessageSourceFactory(
                                        new FileLocalizedResourceProvider(new File("src/test/resources/translation"), propertiesExtensionsFile(), new DirectoryFileFinder()),
                                        new PropertiesLocalizedMessageResourceLoader(new FilenameLocaleExtractor(new RetrieveLocaleExpressionFromFilename()))
                                ))
                                .build()))
                        .and()
                        .build()
        ).render(JtwigModel.newModel());
    }

    @Test
    public void testPersistentCached() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{{ 'hello' | translate ('it') }} - {{ 'hello' | translate ('it') }}",
                configuration()
                        .extensions()
                        .add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(new CachedMessageSourceFactory(
                                        new PersistentMessageSourceCache(new LocaleAndMessageFactory(), new ConcurrentHashMap<LocaleAndMessage, Optional<String>>())
                                        , new AggregatedLocalizedMessageSourceFactory(
                                                new FileLocalizedResourceProvider(new File("src/test/resources/translation"), propertiesFile(), new DirectoryFileFinder()),
                                                new PropertiesLocalizedMessageResourceLoader(new FilenameLocaleExtractor(new RetrieveLocaleExpressionFromFilename()))
                                        )
                                ))
                                .build()))
                        .and()
                        .build()
        ).render(JtwigModel.newModel());

        assertThat(result, is("Ciao - Ciao"));
    }

    @Test
    public void testGuavaCached() throws Exception {
        Cache<LocaleAndMessage, Optional<String>> cache = CacheBuilder.<LocaleAndMessage, Optional<String>>newBuilder().build();
        String result = JtwigTemplate.inlineTemplate("{{ 'hello' | translate ('it') }} - {{ 'hello' | translate ('it') }}",
                configuration()
                        .extensions()
                        .add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(new CachedMessageSourceFactory(
                                        new GuavaMessageSourceCache(new LocaleAndMessageFactory(), new MessageProviderFactory(), cache)
                                        , new AggregatedLocalizedMessageSourceFactory(
                                                new FileLocalizedResourceProvider(new File("src/test/resources/translation"), propertiesFile(), new DirectoryFileFinder()),
                                                new PropertiesLocalizedMessageResourceLoader(new FilenameLocaleExtractor(new RetrieveLocaleExpressionFromFilename()))
                                        )
                                ))
                                .build()))
                        .and()
                        .build()
        ).render(JtwigModel.newModel());

        assertThat(result, is("Ciao - Ciao"));
    }

    @Test
    public void testRecursive() throws Exception {

        String result = JtwigTemplate.inlineTemplate("{{ 'hello' | translate ('fr') }} - {{ 'hello' | translate ('pt') }}",
                configuration()
                        .extensions()
                        .add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(new AggregatedLocalizedMessageSourceFactory(
                                        new CompositeLocalizedResourceProvider(Collections.<LocalizedResourceProvider>singletonList(new FileLocalizedResourceProvider(new File("src/test/resources/translation"), propertiesFile(), new RecursiveFileFinder()))),
                                        new PropertiesLocalizedMessageResourceLoader(new FilenameLocaleExtractor(new RetrieveLocaleExpressionFromFilename()))
                                ))
                                .build()))
                        .and()
                        .build()
        ).render(JtwigModel.newModel());

        assertThat(result, is("Salut - Ola"));

    }

    private FileFilter propertiesFile() {
        return new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.endsWith(".properties")
                        && name.substring(0, name.lastIndexOf('.')).contains(".");
            }
        };
    }

    private FileFilter propertiesExtensionsFile() {
        return new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.endsWith(".properties");
            }
        };
    }
}
