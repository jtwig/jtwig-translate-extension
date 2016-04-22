package org.jtwig.translate.function;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.i18n.decorate.MessageDecorator;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.translate.function.extract.TranslateParameterExtractor;

import java.util.Collection;
import java.util.Collections;

import static java.util.Arrays.asList;

public class TranslateFunction implements JtwigFunction {
    private final TranslateParameterExtractor translateParameterExtractor;
    private final Translator translator;

    public TranslateFunction(TranslateParameterExtractor translateParameterExtractor, Translator translator) {
        this.translateParameterExtractor = translateParameterExtractor;
        this.translator = translator;
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
        TranslateParameterExtractor.TranslateChoiceParameters parameters;

        if (request.getNumberOfArguments() == 2) {
            parameters = translateParameterExtractor.extractForOneExtraArgument(request, request.get(1));
        } else if (request.getNumberOfArguments() == 3) {
            parameters = translateParameterExtractor.extractForTwoExtraArguments(request, request.get(1), request.get(2));
        } else {
            parameters = translateParameterExtractor.extractNoExtraArguments(request);
        }

        return translator.translate(request.getEnvironment(), message, parameters.getLocale(),
                        Collections.<MessageDecorator>singletonList(new ReplacementMessageDecorator(parameters.getReplacements())));
    }
}
