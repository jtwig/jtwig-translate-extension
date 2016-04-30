package org.jtwig.translate.message.source.cache;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.translate.message.source.MessageSource;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessage;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessageFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class PersistentMessageSourceCacheTest {
    private final ConcurrentHashMap<LocaleAndMessage, Optional<String>> concurrentHashMap = new ConcurrentHashMap<LocaleAndMessage, Optional<String>>();
    private final LocaleAndMessageFactory localeAndMessageFactory = mock(LocaleAndMessageFactory.class);
    private PersistentMessageSourceCache underTest = new PersistentMessageSourceCache(
            localeAndMessageFactory,
            concurrentHashMap
    );

    @Before
    public void setUp() throws Exception {
        concurrentHashMap.clear();
    }

    @Test
    public void retrieveIfDefined() throws Exception {
        String message = "message";
        Locale locale = Locale.CANADA;
        Environment environment = mock(Environment.class);
        MessageSource messageSource = mock(MessageSource.class);

        Optional<String> value = Optional.of("result");
        LocaleAndMessage localeAndMessage = new LocaleAndMessage(locale, message);
        when(localeAndMessageFactory.create(locale, message)).thenReturn(localeAndMessage);
        concurrentHashMap.put(localeAndMessage, value);

        Optional<String> result = underTest.retrieve(locale, message, messageSource);

        assertSame(value, result);
        verifyZeroInteractions(messageSource);
    }

    @Test
    public void retrieveIfNotDefined() throws Exception {
        String message = "message";
        Locale locale = Locale.CANADA;
        Environment environment = mock(Environment.class);
        MessageSource messageSource = mock(MessageSource.class);

        Optional<String> value = Optional.of("result");
        LocaleAndMessage localeAndMessage = new LocaleAndMessage(locale, message);

        when(localeAndMessageFactory.create(locale, message)).thenReturn(localeAndMessage);
        when(messageSource.message(locale, message)).thenReturn(value);

        Optional<String> result = underTest.retrieve(locale, message, messageSource);

        assertSame(value, result);
        assertSame(concurrentHashMap.get(localeAndMessage), result);
    }
}