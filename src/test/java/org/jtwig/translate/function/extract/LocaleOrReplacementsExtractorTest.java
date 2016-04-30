package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.translate.message.decorate.ReplacementMessageDecorator;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocaleOrReplacementsExtractorTest {
    private final LocaleExtractor localeExtractor = mock(LocaleExtractor.class);
    private final ReplacementsExtractor replacementsExtractor = mock(ReplacementsExtractor.class);
    private LocaleOrReplacementsExtractor underTest = new LocaleOrReplacementsExtractor(localeExtractor, replacementsExtractor);

    @Test
    public void extractorLocalePresent() throws Exception {
        Locale locale = Locale.CANADA;
        Environment environment = mock(Environment.class);
        Object input = new Object();

        when(localeExtractor.extract(environment, input)).thenReturn(Optional.of(locale));

        LocaleOrReplacementsExtractor.Result result = underTest.extractor(environment, input);

        assertThat(result.getLocale().get(), is(locale));
        assertThat(result.getReplacements().isPresent(), is(false));
    }

    @Test
    public void extractorWhenReplacementsPresent() throws Exception {
        Collection<ReplacementMessageDecorator.Replacement> replacements = Collections.singletonList(mock(ReplacementMessageDecorator.Replacement.class));
        Locale locale = Locale.CANADA;
        Environment environment = mock(Environment.class);
        Object input = new Object();

        when(localeExtractor.extract(environment, input)).thenReturn(Optional.<Locale>absent());
        when(replacementsExtractor.extract(environment, input)).thenReturn(Optional.of(replacements));

        LocaleOrReplacementsExtractor.Result result = underTest.extractor(environment, input);

        assertThat(result.getLocale().isPresent(), is(false));
        assertThat(result.getReplacements().get(), is(replacements));
    }

    @Test
    public void extractorWhenNonePresent() throws Exception {
        Environment environment = mock(Environment.class);
        Object input = new Object();

        when(localeExtractor.extract(environment, input)).thenReturn(Optional.<Locale>absent());
        when(replacementsExtractor.extract(environment, input)).thenReturn(Optional.<Collection<ReplacementMessageDecorator.Replacement>>absent());

        LocaleOrReplacementsExtractor.Result result = underTest.extractor(environment, input);

        assertThat(result.getLocale().isPresent(), is(false));
        assertThat(result.getReplacements().isPresent(), is(false));
    }
}