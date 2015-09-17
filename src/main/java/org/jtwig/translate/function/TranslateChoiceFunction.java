package org.jtwig.translate.function;

import com.google.common.base.Supplier;
import org.jtwig.context.RenderContext;
import org.jtwig.context.RenderContextHolder;
import org.jtwig.environment.Environment;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.functions.JtwigFunctionRequest;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.translate.decorator.PluralSelector;
import org.jtwig.value.JtwigValueFactory;
import org.jtwig.value.configuration.ValueConfiguration;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Arrays.asList;

public class TranslateChoiceFunction implements JtwigFunction {
    @Override
    public String name() {
        return "translateChoice";
    }

    @Override
    public Collection<String> aliases() {
        return asList("transchoice");
    }

    @Override
    public Object execute(JtwigFunctionRequest request) {
        request.minimumNumberOfArguments(2).maximumNumberOfArguments(4);
        String message = request.getArgument(0, String.class);
        BigDecimal count = request.getArgument(1, BigDecimal.class);
        Map<Object, Object> replacements = Collections.emptyMap();
        Locale locale = getLocaleSupplier().get();

        if (request.getNumberOfArguments() == 3) {
            if (request.get(2).as(Map.class).isPresent()) {
                replacements = request.getArgument(2, Map.class);
            } else {
                locale = request.getArgument(2, Locale.class);
            }
        } else if (request.getNumberOfArguments() == 4) {
            replacements = request.getArgument(2, Map.class);
            locale = request.getArgument(3, Locale.class);
        }

        return translateChoice(message, count, replacements, locale);
    }



    public String translateChoice (String message, BigDecimal count, Map replacements, Locale locale) {
        return new Translator(getEnvironment())
                .translate(message, locale, asList(
                        new PluralSelector(count.intValue()),
                        new ReplacementMessageDecorator(toReplacementCollection(replacements, getValueConfiguration()))
                ));
    }

    private Supplier<Locale> getLocaleSupplier() {
        return TranslateConfiguration.currentLocaleSupplier(getRenderContext().environment());
    }

    private Environment getEnvironment() {
        return getRenderContext().environment();
    }

    private ValueConfiguration getValueConfiguration() {
        return getRenderContext().environment().valueConfiguration();
    }

    private Collection<ReplacementMessageDecorator.Replacement> toReplacementCollection(Map<Object, Object> replacements, ValueConfiguration valueConfiguration) {
        Collection<ReplacementMessageDecorator.Replacement> result = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : replacements.entrySet()) {
            if (entry.getKey() != null) {
                String key = entry.getKey().toString();
                String stringValue = JtwigValueFactory.value(entry.getValue(), valueConfiguration).asString();
                result.add(new ReplacementMessageDecorator.Replacement(key, stringValue));
            }
        }
        return result;
    }

    protected RenderContext getRenderContext() {
        return RenderContextHolder.get();
    }
}
