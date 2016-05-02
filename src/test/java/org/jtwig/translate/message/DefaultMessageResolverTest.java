package org.jtwig.translate.message;

import com.google.common.base.Optional;
import org.jtwig.translate.message.decorate.MessageDecorator;
import org.jtwig.translate.message.source.MessageSource;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultMessageResolverTest {
    private final MessageSource messageSource = mock(MessageSource.class);
    private DefaultMessageResolver underTest = new DefaultMessageResolver(messageSource);

    @Test
    public void resolveIfMessageIsNull() throws Exception {
        Locale locale = Locale.CANADA;

        when(messageSource.message(locale, null)).thenReturn(Optional.<String>absent());

        Optional<String> result = underTest.resolve(locale, null, mock(MessageDecorator.class));

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void resolveIfMessageIsNotNull() throws Exception {
        Locale locale = Locale.CANADA;
        String message = "  message  ";
        MessageDecorator decorator = mock(MessageDecorator.class);

        when(messageSource.message(locale, "message")).thenReturn(Optional.<String>absent());

        Optional<String> result = underTest.resolve(locale, message, decorator);

        assertThat(result.isPresent(), is(false));
    }
}