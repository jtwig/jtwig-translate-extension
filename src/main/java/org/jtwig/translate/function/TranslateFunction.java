package org.jtwig.translate.function;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import org.jtwig.environment.Environment;
import org.jtwig.exceptions.CalculationException;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.i18n.decorate.MessageDecorator;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.translate.function.extract.LocaleExtractor;
import org.jtwig.translate.function.extract.ReplacementsExtractor;
import org.jtwig.util.ErrorMessageFormatter;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import static java.util.Arrays.asList;

public class TranslateFunction implements JtwigFunction {
    private final LocaleExtractor localeExtractor;
    private final ReplacementsExtractor replacementsExtractor;

    public TranslateFunction(LocaleExtractor localeExtractor, ReplacementsExtractor replacementsExtractor) {
        this.localeExtractor = localeExtractor;
        this.replacementsExtractor = replacementsExtractor;
    }

    @Override
    public String name() {
        return "translate";
    }

    @Override
    public Collection<String> aliases() {
        return asList("trans", "message");
    }

    @Override
    public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(1).maximumNumberOfArguments(3);
        String message = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
        Collection<ReplacementMessageDecorator.Replacement> replacements = Collections.emptyList();
        Locale locale = getLocaleSupplier(request.getEnvironment()).get();

        if (request.getNumberOfArguments() == 2) {
            Optional<Locale> localeExtract = localeExtractor.extract(request.getEnvironment(), request.get(1));
            if (localeExtract.isPresent()) {
                locale = localeExtract.get();
            } else {
                Optional<Collection<ReplacementMessageDecorator.Replacement>> collectionOptional = replacementsExtractor.extract(request.getEnvironment(), request.get(1));
                if (collectionOptional.isPresent()) {
                    replacements = collectionOptional.get();
                } else {
                    throw new CalculationException(ErrorMessageFormatter.errorMessage(request.getPosition(), String.format("Expecting map or locale as second argument, but got '%s'", request.get(1))));
                }
            }
        } else if (request.getNumberOfArguments() == 3) {
            Optional<Collection<ReplacementMessageDecorator.Replacement>> collectionOptional = replacementsExtractor.extract(request.getEnvironment(), request.get(1));
            if (collectionOptional.isPresent()) {
                replacements = collectionOptional.get();
            } else {
                throw new CalculationException(ErrorMessageFormatter.errorMessage(request.getPosition(), String.format("Expecting map as second argument, but got '%s'", request.get(1))));
            }
            Optional<Locale> localeExtract = localeExtractor.extract(request.getEnvironment(), request.get(2));
            if (localeExtract.isPresent()) {
                locale = localeExtract.get();
            } else {
                throw new CalculationException(ErrorMessageFormatter.errorMessage(request.getPosition(), String.format("Expecting locale as third argument, but got '%s'", request.get(2))));
            }
        }

        return new Translator(request.getEnvironment()).translate(message, locale, Collections.<MessageDecorator>singletonList(new ReplacementMessageDecorator(replacements)));
    }

    private Supplier<Locale> getLocaleSupplier(Environment environment) {
        return TranslateConfiguration.currentLocaleSupplier(environment);
    }
}
