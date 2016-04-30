package org.jtwig.translate.message.source;

import com.google.common.base.Optional;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InMemoryMessageSourceTest {
    private final HashMap<Locale, LocalizedMessageResource> sources = new HashMap<>();
    private InMemoryMessageSource underTest = new InMemoryMessageSource(sources);

    @Test
    public void whenNotPresent() throws Exception {
        Locale locale = Locale.CANADA;
        String message = "message";

        Optional<String> result = underTest.message(locale, message);

        assertThat(result.isPresent(), is(false));
    }
}