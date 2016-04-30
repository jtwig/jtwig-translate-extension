package org.jtwig.translate.message.source.localized;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AggregatedLocalizedMessageSourceTest {
    private final HashMap<Locale, Collection<LocalizedMessageResource>> messageResources = new HashMap<Locale, Collection<LocalizedMessageResource>>();
    private final AggregatedLocalizedMessageSource underTest = new AggregatedLocalizedMessageSource(messageResources);

    @Before
    public void setUp() throws Exception {
        messageResources.clear();
    }

    @Test
    public void messageIfNoLocale() throws Exception {
        Environment environment = mock(Environment.class);
        Locale locale = Locale.CANADA;
        String message = "test";

        Optional<String> result = underTest.message(locale, message);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void messageIfLocaleButNoMessage() throws Exception {
        String message = "test";
        Locale locale = Locale.CANADA;

        LocalizedMessageResource localizedMessageResource = mock(LocalizedMessageResource.class);
        Environment environment = mock(Environment.class);

        messageResources.put(locale, Collections.singletonList(localizedMessageResource));
        when(localizedMessageResource.resolve(message)).thenReturn(Optional.<String>absent());

        Optional<String> result = underTest.message(locale, message);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void messageIfLocaleAndMessage() throws Exception {
        String message = "test";
        Locale locale = Locale.CANADA;

        LocalizedMessageResource localizedMessageResource = mock(LocalizedMessageResource.class);
        Environment environment = mock(Environment.class);

        messageResources.put(locale, Collections.singletonList(localizedMessageResource));
        when(localizedMessageResource.resolve(message)).thenReturn(Optional.of("result"));

        Optional<String> result = underTest.message(locale, message);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is("result"));
    }
}