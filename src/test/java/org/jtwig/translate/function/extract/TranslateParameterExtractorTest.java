package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import org.jtwig.environment.Environment;
import org.jtwig.exceptions.CalculationException;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class TranslateParameterExtractorTest {
    private final LocaleExtractor localeExtractor = mock(LocaleExtractor.class);
    private final ReplacementsExtractor replacementsExtractor = mock(ReplacementsExtractor.class);
    private final LocaleOrReplacementsExtractor localeOrReplacementsExtractor = mock(LocaleOrReplacementsExtractor.class);
    private TranslateParameterExtractor underTest = new TranslateParameterExtractor(localeExtractor, replacementsExtractor, localeOrReplacementsExtractor);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void extractChoiceForTwoArguments() throws Exception {
        Locale locale = Locale.CANADA;
        FunctionRequest request = mock(FunctionRequest.class, RETURNS_DEEP_STUBS);
        Supplier<Locale> supplier = mock(Supplier.class);
        when(request.getEnvironment().parameter("currentLocaleSupplier")).thenReturn(supplier);
        when(supplier.get()).thenReturn(locale);

        TranslateParameterExtractor.TranslateChoiceParameters result =
                underTest.extractNoExtraArguments(request);

        assertThat(result.getLocale(), is(locale));
        assertThat(result.getReplacements(), is(empty()));
    }

    @Test
    public void extractForThreeArgumentsWhenNoArgumentPresent() throws Exception {
        Object value = new Object();
        FunctionRequest request = mock(FunctionRequest.class, RETURNS_DEEP_STUBS);
        Environment environment = mock(Environment.class);

        when(request.getEnvironment()).thenReturn(environment);
        when(localeOrReplacementsExtractor.extractor(request.getEnvironment(), value))
                .thenReturn(new LocaleOrReplacementsExtractor.Result(
                        Optional.<Locale>absent(),
                        Optional.<Collection<ReplacementMessageDecorator.Replacement>>absent()));

        expectedException.expect(CalculationException.class);
        expectedException.expectMessage(containsString(String.format("Expecting map or locale, but got '%s'", value)));

        underTest.extractForOneExtraArgument(request, value);
    }

    @Test
    public void extractForThreeArgumentsWhenLocalePresent() throws Exception {
        Object value = new Object();
        FunctionRequest request = mock(FunctionRequest.class, RETURNS_DEEP_STUBS);
        Environment environment = mock(Environment.class);
        Supplier<Locale> supplier = mock(Supplier.class);
        Locale locale = Locale.CANADA;

        when(request.getEnvironment()).thenReturn(environment);
        when(environment.parameter("currentLocaleSupplier")).thenReturn(supplier);
        when(request.get(2)).thenReturn(value);
        when(localeOrReplacementsExtractor.extractor(request.getEnvironment(), value))
                .thenReturn(new LocaleOrReplacementsExtractor.Result(
                        Optional.of(locale),
                        Optional.<Collection<ReplacementMessageDecorator.Replacement>>absent()));

        TranslateParameterExtractor.TranslateChoiceParameters result = underTest.extractForOneExtraArgument(request, value);

        assertThat(result.getLocale(), is(locale));
        assertThat(result.getReplacements(), is(empty()));
    }

    @Test
    public void extractForThreeArgumentsWhenReplacementsPresent() throws Exception {
        Collection<ReplacementMessageDecorator.Replacement> replacements = Collections.singletonList(mock(ReplacementMessageDecorator.Replacement.class));
        Object value = new Object();
        FunctionRequest request = mock(FunctionRequest.class, RETURNS_DEEP_STUBS);
        Environment environment = mock(Environment.class);
        Supplier<Locale> supplier = mock(Supplier.class);
        Locale locale = Locale.CANADA;

        when(supplier.get()).thenReturn(locale);
        when(request.getEnvironment()).thenReturn(environment);
        when(environment.parameter("currentLocaleSupplier")).thenReturn(supplier);
        when(localeOrReplacementsExtractor.extractor(request.getEnvironment(), value))
                .thenReturn(new LocaleOrReplacementsExtractor.Result(
                        Optional.<Locale>absent(),
                        Optional.of(replacements)));

        TranslateParameterExtractor.TranslateChoiceParameters result = underTest.extractForOneExtraArgument(request, value);

        assertThat(result.getLocale(), is(locale));
        assertThat(result.getReplacements(), is(replacements));
    }

    @Test
    public void extractForFourArgumentsWhenReplacementsNotPresent() throws Exception {
        Object argument2 = new Object();
        FunctionRequest request = mock(FunctionRequest.class, RETURNS_DEEP_STUBS);
        Environment environment = mock(Environment.class);

        when(request.getEnvironment()).thenReturn(environment);
        when(replacementsExtractor.extract(environment, argument2)).thenReturn(Optional.<Collection<ReplacementMessageDecorator.Replacement>>absent());

        expectedException.expect(CalculationException.class);
        expectedException.expectMessage(containsString(String.format("Expecting map, but got '%s'", argument2)));

        underTest.extractForTwoExtraArguments(request, argument2, new Object());
    }

    @Test
    public void extractForFourArgumentsWhenLocaleNotPresent() throws Exception {
        Collection<ReplacementMessageDecorator.Replacement> replacements = Collections.singletonList(mock(ReplacementMessageDecorator.Replacement.class));
        Object argument2 = new Object();
        Object argument3 = new Object();
        FunctionRequest request = mock(FunctionRequest.class, RETURNS_DEEP_STUBS);
        Environment environment = mock(Environment.class);

        when(request.getEnvironment()).thenReturn(environment);
        when(replacementsExtractor.extract(environment, argument2)).thenReturn(Optional.of(replacements));
        when(localeExtractor.extract(environment, argument3)).thenReturn(Optional.<Locale>absent());

        expectedException.expect(CalculationException.class);
        expectedException.expectMessage(containsString(String.format("Expecting locale, but got '%s'", argument3)));

        underTest.extractForTwoExtraArguments(request, argument2, argument3);
    }

    @Test
    public void extractForFourArgumentsWhenBothPresent() throws Exception {
        Collection<ReplacementMessageDecorator.Replacement> replacements = Collections.singletonList(mock(ReplacementMessageDecorator.Replacement.class));
        Object argument2 = new Object();
        Object argument3 = new Object();
        Locale locale = Locale.CANADA;
        FunctionRequest request = mock(FunctionRequest.class, RETURNS_DEEP_STUBS);
        Environment environment = mock(Environment.class);

        when(request.getEnvironment()).thenReturn(environment);
        when(replacementsExtractor.extract(environment, argument2)).thenReturn(Optional.of(replacements));
        when(localeExtractor.extract(environment, argument3)).thenReturn(Optional.of(locale));

        TranslateParameterExtractor.TranslateChoiceParameters result = underTest.extractForTwoExtraArguments(request, argument2, argument3);

        assertThat(result.getLocale(), is(locale));
        assertThat(result.getReplacements(), is(replacements));
    }
}