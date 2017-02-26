package org.jtwig.translate.render.node;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import org.jtwig.environment.Environment;
import org.jtwig.escape.EscapeEngine;
import org.jtwig.exceptions.CalculationException;
import org.jtwig.render.RenderRequest;
import org.jtwig.render.node.renderer.NodeRender;
import org.jtwig.renderable.Renderable;
import org.jtwig.renderable.StringBuilderRenderResult;
import org.jtwig.renderable.impl.StringRenderable;
import org.jtwig.translate.TranslateExtension;
import org.jtwig.translate.function.extract.LocaleExtractor;
import org.jtwig.translate.function.extract.ReplacementsExtractor;
import org.jtwig.translate.message.decorate.CompositeMessageDecorator;
import org.jtwig.translate.message.decorate.ExpressionMessageDecorator;
import org.jtwig.translate.message.decorate.MessageDecorator;
import org.jtwig.translate.message.decorate.ReplacementMessageDecorator;
import org.jtwig.translate.node.TranslateNode;
import org.jtwig.util.ErrorMessageFormatter;
import org.jtwig.value.context.ValueContext;
import org.jtwig.value.environment.ValueEnvironment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class TranslateNodeRender implements NodeRender<TranslateNode> {
    private final LocaleExtractor localeExtractor;
    private final ReplacementsExtractor replacementsExtractor;

    public TranslateNodeRender(LocaleExtractor localeExtractor, ReplacementsExtractor replacementsExtractor) {
        this.localeExtractor = localeExtractor;
        this.replacementsExtractor = replacementsExtractor;
    }

    @Override
    public Renderable render(RenderRequest request, TranslateNode node) {
        Environment environment = request.getEnvironment();
        Renderable renderable = environment.getRenderEnvironment().getRenderNodeService().render(request, node.getContent());
        String message = renderable
                .appendTo(new StringBuilderRenderResult())
                .content().trim();

        Locale locale;
        if (node.getLocaleExpression().isPresent()) {
            Object calculatedValue = environment.getRenderEnvironment().getCalculateExpressionService().calculate(request, node.getLocaleExpression().get());
            Optional<Locale> optional = localeExtractor.extract(environment, calculatedValue);

            if (optional.isPresent()) {
                locale = optional.get();
            } else {
                throw new CalculationException(ErrorMessageFormatter.errorMessage(node.getPosition(), String.format("Unable to convert '%s' to locale", calculatedValue)));
            }
        } else {
            locale = TranslateExtension.enviroment(environment).getLocaleSupplier().get();
        }

        Collection<MessageDecorator> decorators = new ArrayList<>();

        if (node.getWithExpression().isPresent()) {
            Object calculatedValue = environment.getRenderEnvironment().getCalculateExpressionService().calculate(request, node.getWithExpression().get());
            Optional<Collection<ReplacementMessageDecorator.Replacement>> optional = replacementsExtractor.extract(environment, calculatedValue);
            if (optional.isPresent()) {
                decorators.add(new ReplacementMessageDecorator(optional.get()));
            } else {
                throw new CalculationException(ErrorMessageFormatter.errorMessage(node.getPosition(), String.format("Unable to convert '%s' to replacements", calculatedValue)));
            }
        }
        decorators.add(new ExpressionMessageDecorator(fromContextValue(request.getRenderContext().getCurrent(ValueContext.class), environment.getValueEnvironment())));

        CompositeMessageDecorator messageDecorator = new CompositeMessageDecorator(decorators);
        return new StringRenderable(TranslateExtension.enviroment(environment).getMessageResolver()
                .resolve(locale, message, messageDecorator)
                .or(defaultMessage(message, messageDecorator)), request.getRenderContext().getCurrent(EscapeEngine.class));
    }


    private Supplier<String> defaultMessage(final String message, final CompositeMessageDecorator messageDecorator) {
        return new Supplier<String>() {
            @Override
            public String get() {
                return messageDecorator.decorate(message);
            }
        };
    }

    private ExpressionMessageDecorator.ReplacementFinder fromContextValue(final ValueContext valueContext, final ValueEnvironment configuration) {
        return new ExpressionMessageDecorator.ReplacementFinder() {
            @Override
            public String replacementFor(String key) {
                return configuration.getStringConverter().convert(valueContext.resolve(key));
            }
        };
    }
}
