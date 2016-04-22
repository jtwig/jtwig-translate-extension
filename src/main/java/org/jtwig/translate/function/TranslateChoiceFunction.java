package org.jtwig.translate.function;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.translate.decorator.PluralSelector;
import org.jtwig.translate.function.extract.TranslateParameterExtractor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

import static java.util.Arrays.asList;

public class TranslateChoiceFunction implements JtwigFunction {
    private final TranslateParameterExtractor translateParameterExtractor;
    private final Translator translator;

    public TranslateChoiceFunction(TranslateParameterExtractor translateParameterExtractor, Translator translator) {
        this.translateParameterExtractor = translateParameterExtractor;
        this.translator = translator;
    }

    @Override
    public String name() {
        return "translateChoice";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.singletonList("transchoice");
    }

    @Override
    public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(2).maximumNumberOfArguments(4);
        String message = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
        BigDecimal count = request.getEnvironment().getValueEnvironment().getNumberConverter().convert(request.get(1))
                .orThrow(request.getPosition(), String.format("Expecting number as second argument but got '%s'", request.get(1)));

        TranslateParameterExtractor.TranslateChoiceParameters parameters;

        if (request.getNumberOfArguments() == 2) {
            parameters = translateParameterExtractor.extractNoExtraArguments(request);
        } else if (request.getNumberOfArguments() == 3) {
            parameters = translateParameterExtractor.extractForOneExtraArgument(request, request.get(2));
        } else {
            parameters = translateParameterExtractor.extractForTwoExtraArguments(request, request.get(2), request.get(3));
        }

        return translator
                .translate(request.getEnvironment(), message, parameters.getLocale(), asList(
                        new PluralSelector(request.getPosition(), count.intValue()),
                        new ReplacementMessageDecorator(parameters.getReplacements())
                ));
    }

}
