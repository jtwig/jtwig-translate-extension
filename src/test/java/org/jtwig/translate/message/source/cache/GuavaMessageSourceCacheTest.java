package org.jtwig.translate.message.source.cache;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import org.jtwig.translate.message.source.MessageSource;
import org.jtwig.translate.message.source.cache.callable.MessageProvider;
import org.jtwig.translate.message.source.cache.callable.MessageProviderFactory;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessage;
import org.jtwig.translate.message.source.cache.model.LocaleAndMessageFactory;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GuavaMessageSourceCacheTest {
    private final LocaleAndMessageFactory localeAndMessageFactory = mock(LocaleAndMessageFactory.class);
    private final MessageProviderFactory messageProviderFactory = mock(MessageProviderFactory.class);
    private final Cache<LocaleAndMessage, Optional<String>> cache = mock(Cache.class);
    private GuavaMessageSourceCache underTest = new GuavaMessageSourceCache(
            localeAndMessageFactory,
            messageProviderFactory,
            cache
    );

    @Test
    public void retrieve() throws Exception {
        Locale locale = Locale.CANADA;
        String message = "message";
        MessageSource messageSource = mock(MessageSource.class);
        MessageProvider messageProvider = mock(MessageProvider.class);
        LocaleAndMessage localeAndMessage = new LocaleAndMessage(locale, message);
        Optional<String> expected = Optional.of("result");

        when(localeAndMessageFactory.create(locale, message)).thenReturn(localeAndMessage);
        when(messageProviderFactory.create(locale, message, messageSource)).thenReturn(messageProvider);
        when(cache.get(localeAndMessage, messageProvider)).thenReturn(expected);

        Optional<String> result = underTest.retrieve(locale, message, messageSource);

        assertSame(expected, result);
    }
}