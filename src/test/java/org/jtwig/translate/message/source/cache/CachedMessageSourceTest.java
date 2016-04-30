package org.jtwig.translate.message.source.cache;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.MessageSource;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CachedMessageSourceTest {
    private final MessageSourceCache messageSourceCache = mock(MessageSourceCache.class);
    private final MessageSource messageSource = mock(MessageSource.class);
    private CachedMessageSource underTest = new CachedMessageSource(messageSourceCache, messageSource);

    @Test
    public void message() throws Exception {
        Locale locale = Locale.CHINA;
        String message = "message";

        Optional<String> optional = Optional.of("value");
        when(messageSourceCache.retrieve(locale, message, messageSource))
                .thenReturn(optional);

        Optional<String> result = underTest.message(locale, message);

        assertSame(optional, result);
    }
}