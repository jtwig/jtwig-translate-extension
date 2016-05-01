package org.jtwig.translate.message.source;

import org.jtwig.translate.message.source.factory.MessageSourceFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Locale;

import static org.mockito.Mockito.mock;

public class InitializableMessageSourceTest {
    private final MessageSourceFactory messageSourceFactory = mock(MessageSourceFactory.class);
    private InitializableMessageSource underTest = new InitializableMessageSource(messageSourceFactory);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void message() throws Exception {
        String message = "message";
        Locale locale = Locale.CANADA;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Message source not loaded. Is this part of the environment initializers list?");

        underTest.message(locale, message);
    }
}