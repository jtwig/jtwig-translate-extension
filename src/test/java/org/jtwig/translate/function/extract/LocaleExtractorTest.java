package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.i18n.locale.LocaleResolver;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class LocaleExtractorTest {
    private LocaleExtractor underTest = new LocaleExtractor();

    @Test
    public void extractLocale() throws Exception {
        Environment environment = mock(Environment.class);

        Optional<Locale> result = underTest.extract(environment, Locale.CANADA);

        assertSame(Locale.CANADA, result.get());
    }

    @Test
    public void extractString() throws Exception {
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        LocaleResolver localeResolver = mock(LocaleResolver.class);

        when(environment.parameter("localeResolver")).thenReturn(localeResolver);
        when(localeResolver.resolve("hello")).thenReturn(Locale.CANADA);

        Optional<Locale> result = underTest.extract(environment, "hello");

        assertSame(Locale.CANADA, result.get());
    }
    @Test
    public void extractStringNone() throws Exception {
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);

        Optional<Locale> result = underTest.extract(environment, new Object());

        assertSame(false, result.isPresent());
    }
}