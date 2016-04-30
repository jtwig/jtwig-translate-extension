package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import org.jtwig.exceptions.CalculationException;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.translate.message.decorate.ReplacementMessageDecorator;
import org.jtwig.util.ErrorMessageFormatter;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import static org.jtwig.translate.TranslateExtension.enviroment;

public class TranslateParameterExtractor {
    private final LocaleExtractor localeExtractor;
    private final ReplacementsExtractor replacementsExtractor;
    private final LocaleOrReplacementsExtractor localeOrReplacementsExtractor;

    public TranslateParameterExtractor(LocaleExtractor localeExtractor, ReplacementsExtractor replacementsExtractor, LocaleOrReplacementsExtractor localeOrReplacementsExtractor) {
        this.localeExtractor = localeExtractor;
        this.replacementsExtractor = replacementsExtractor;
        this.localeOrReplacementsExtractor = localeOrReplacementsExtractor;
    }

    public TranslateChoiceParameters extractNoExtraArguments(FunctionRequest request) {
        return new TranslateChoiceParameters(
                enviroment(request.getEnvironment()).getLocaleSupplier().get(),
                Collections.<ReplacementMessageDecorator.Replacement>emptyList()
        );
    }

    public TranslateChoiceParameters extractForOneExtraArgument(FunctionRequest request, Object input) {
        LocaleOrReplacementsExtractor.Result result = localeOrReplacementsExtractor.extractor(request.getEnvironment(), input);
        if (result.isEmpty()) {
            throw new CalculationException(ErrorMessageFormatter.errorMessage(request.getPosition(), String.format("Expecting map or locale, but got '%s'", input)));
        } else {
            Locale locale = result.getLocale().or(enviroment(request.getEnvironment()).getLocaleSupplier());
            Collection<ReplacementMessageDecorator.Replacement> empty = Collections.emptyList();
            Collection<ReplacementMessageDecorator.Replacement> replacements = result.getReplacements().or(empty);

            return new TranslateChoiceParameters(locale, replacements);
        }
    }

    public TranslateChoiceParameters extractForTwoExtraArguments(FunctionRequest request, Object firstArgument, Object secondArgument) {
        Collection<ReplacementMessageDecorator.Replacement> replacements;
        Locale locale;

        Optional<Collection<ReplacementMessageDecorator.Replacement>> collectionOptional = replacementsExtractor.extract(request.getEnvironment(), firstArgument);
        if (collectionOptional.isPresent()) {
            replacements = collectionOptional.get();
        } else {
            throw new CalculationException(ErrorMessageFormatter.errorMessage(request.getPosition(), String.format("Expecting map, but got '%s'", firstArgument)));
        }

        Optional<Locale> localeExtract = localeExtractor.extract(request.getEnvironment(), secondArgument);
        if (localeExtract.isPresent()) {
            locale = localeExtract.get();
        } else {
            throw new CalculationException(ErrorMessageFormatter.errorMessage(request.getPosition(), String.format("Expecting locale, but got '%s'", secondArgument)));
        }

        return new TranslateChoiceParameters(locale, replacements);
    }

    public static class TranslateChoiceParameters {
        private final Locale locale;
        private final Collection<ReplacementMessageDecorator.Replacement> replacements;

        public TranslateChoiceParameters(Locale locale, Collection<ReplacementMessageDecorator.Replacement> replacements) {
            this.locale = locale;
            this.replacements = replacements;
        }

        public Locale getLocale() {
            return locale;
        }

        public Collection<ReplacementMessageDecorator.Replacement> getReplacements() {
            return replacements;
        }
    }
}
