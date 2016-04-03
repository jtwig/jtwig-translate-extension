package org.jtwig.translate.function;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import org.jtwig.environment.Environment;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.translate.decorator.PluralSelector;
import org.jtwig.translate.function.extract.LocaleExtractor;
import org.jtwig.translate.function.extract.ReplacementsExtractor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import static java.util.Arrays.asList;

public class TranslateChoiceFunction implements JtwigFunction {
    private final LocaleExtractor localeExtractor;
    private final ReplacementsExtractor replacementsExtractor;

    public TranslateChoiceFunction(LocaleExtractor localeExtractor, ReplacementsExtractor replacementsExtractor) {
        this.localeExtractor = localeExtractor;
        this.replacementsExtractor = replacementsExtractor;
    }

    @Override
    public String name() {
        return "translateChoice";
    }

    @Override
    public Collection<String> aliases() {
        return asList("transchoice");
    }

    @Override
    public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(2).maximumNumberOfArguments(4);
        String message = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
        BigDecimal count = request.getEnvironment().getValueEnvironment().getNumberConverter().convert(request.get(1)).or(BigDecimal.ZERO);
        Collection<ReplacementMessageDecorator.Replacement> replacements = Collections.emptyList();
        Locale locale = getLocaleSupplier(request.getEnvironment()).get();

        if (request.getNumberOfArguments() == 3) {
            Optional<Locale> localeExtract = localeExtractor.extract(request.getEnvironment(), request.get(2));
            if (localeExtract.isPresent()) {
                locale = localeExtract.get();
            } else {
                Optional<Collection<ReplacementMessageDecorator.Replacement>> collectionOptional = replacementsExtractor.extract(request.getEnvironment(), request.get(2));
                if (collectionOptional.isPresent()) {
                    replacements = collectionOptional.get();
                }
            }
        } else if (request.getNumberOfArguments() == 4) {
            Optional<Collection<ReplacementMessageDecorator.Replacement>> collectionOptional = replacementsExtractor.extract(request.getEnvironment(), request.get(2));
            if (collectionOptional.isPresent()) {
                replacements = collectionOptional.get();
            }
            Optional<Locale> localeExtract = localeExtractor.extract(request.getEnvironment(), request.get(3));
            if (localeExtract.isPresent()) {
                locale = localeExtract.get();
            }
        }

        return new Translator(request.getEnvironment())
                .translate(message, locale, asList(
                        new PluralSelector(request.getPosition(), count.intValue()),
                        new ReplacementMessageDecorator(replacements)
                ));
    }

    private Supplier<Locale> getLocaleSupplier(Environment environment) {
        return TranslateConfiguration.currentLocaleSupplier(environment);
    }
}
