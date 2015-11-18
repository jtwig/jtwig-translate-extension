package org.jtwig.translate.function;

import com.google.common.base.Supplier;
import org.jtwig.context.RenderContext;
import org.jtwig.context.RenderContextHolder;
import org.jtwig.environment.Environment;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.functions.JtwigFunctionRequest;
import org.jtwig.i18n.decorate.MessageDecorator;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.value.JtwigValueFactory;
import org.jtwig.value.environment.ValueEnvironment;

import java.util.*;

import static java.util.Arrays.asList;

public class TranslateFunction implements JtwigFunction {
    @Override
    public String name() {
        return "translate";
    }

    @Override
    public Collection<String> aliases() {
        return asList("trans", "message");
    }

    @Override
    public Object execute(JtwigFunctionRequest request) {
        request.minimumNumberOfArguments(1).maximumNumberOfArguments(3);
        String message = request.getArgument(0, String.class);
        Map<Object, Object> replacements = Collections.emptyMap();
        Locale locale = getLocaleSupplier().get();

        if (request.getNumberOfArguments() == 2) {
            if (request.get(1).as(Map.class).isPresent()) {
                replacements = request.getArgument(1, Map.class);
            } else {
                locale = request.getArgument(1, Locale.class);
            }
        } else if (request.getNumberOfArguments() == 3) {
            replacements = request.getArgument(1, Map.class);
            locale = request.getArgument(2, Locale.class);
        }

        return translate(message, replacements, locale);
    }

    public String translate (String message, Map replacements, Locale locale) {

        return new Translator(getEnvironment())
                .translate(message, locale, Collections.<MessageDecorator>singletonList(new ReplacementMessageDecorator(toReplacementCollection(replacements, getValueConfiguration()))));
    }

    protected RenderContext getRenderContext() {
        return RenderContextHolder.get();
    }
    private ValueEnvironment getValueConfiguration() {
        return getRenderContext().environment().value();
    }
    private Environment getEnvironment() {
        return getRenderContext().environment();
    }
    private Supplier<Locale> getLocaleSupplier() {
        return TranslateConfiguration.currentLocaleSupplier(getRenderContext().environment());
    }

    private Collection<ReplacementMessageDecorator.Replacement> toReplacementCollection(Map<Object, Object> replacements, ValueEnvironment valueEnvironment) {
        Collection<ReplacementMessageDecorator.Replacement> result = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : replacements.entrySet()) {
            if (entry.getKey() != null) {
                String key = entry.getKey().toString();
                String stringValue = JtwigValueFactory.value(entry.getValue(), valueEnvironment).asString();
                result.add(new ReplacementMessageDecorator.Replacement(key, stringValue));
            }
        }
        return result;
    }
}
