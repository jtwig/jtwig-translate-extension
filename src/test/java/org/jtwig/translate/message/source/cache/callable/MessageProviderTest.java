package org.jtwig.translate.message.source.cache.callable;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.MessageSource;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageProviderTest {
    private final MessageSource messageSource = mock(MessageSource.class);
    private final Locale locale = Locale.CANADA;
    private final String message = "message";
    private MessageProvider underTest = new MessageProvider(locale, message, messageSource);

    @Test
    public void call() throws Exception {
        Optional<String> expected = Optional.of("result");
        when(messageSource.message(locale, message)).thenReturn(expected);

        Optional<String> result = underTest.call();

        assertSame(expected, result);
    }
}