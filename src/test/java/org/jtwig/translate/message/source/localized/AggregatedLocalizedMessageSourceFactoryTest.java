package org.jtwig.translate.message.source.localized;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.translate.message.source.localized.loader.LocalizedMessageResourceLoader;
import org.jtwig.translate.message.source.localized.provider.LocalizedResourceProvider;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;
import org.junit.Test;

import java.util.Collections;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AggregatedLocalizedMessageSourceFactoryTest {
    private final LocalizedResourceProvider localizedResourceProvider = mock(LocalizedResourceProvider.class);
    private final LocalizedMessageResourceLoader localizedMessageResourceLoader = mock(LocalizedMessageResourceLoader.class);

    private AggregatedLocalizedMessageSourceFactory underTest = new AggregatedLocalizedMessageSourceFactory(
            localizedResourceProvider,
            localizedMessageResourceLoader
    );

    @Test
    public void create() throws Exception {
        Locale locale = Locale.CANADA;
        Optional<String> expected = Optional.of("test");

        Environment environment = mock(Environment.class);
        ResourceReference resourceReference = mock(ResourceReference.class);
        LocalizedMessageResource messageResource = mock(LocalizedMessageResource.class);

        when(localizedResourceProvider.retrieve(environment)).thenReturn(Collections.singletonList(resourceReference));
        when(localizedMessageResourceLoader.load(environment, resourceReference)).thenReturn(messageResource);
        when(messageResource.getLocale()).thenReturn(locale);
        when(messageResource.resolve("blah")).thenReturn(expected);

        AggregatedLocalizedMessageSource result = underTest.create(environment);

        assertThat(result.message(locale, "blah"), is(expected));
        assertThat(result.message(Locale.CHINA, "blah"), is(Optional.<String>absent()));
    }
}