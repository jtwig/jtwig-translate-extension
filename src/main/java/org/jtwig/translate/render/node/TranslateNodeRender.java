package org.jtwig.translate.render.node;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import org.jtwig.exceptions.CalculationException;
import org.jtwig.i18n.decorate.CompositeMessageDecorator;
import org.jtwig.i18n.decorate.ExpressionMessageDecorator;
import org.jtwig.i18n.decorate.MessageDecorator;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.render.RenderRequest;
import org.jtwig.render.node.renderer.NodeRender;
import org.jtwig.renderable.Renderable;
import org.jtwig.renderable.StringBuilderRenderResult;
import org.jtwig.renderable.impl.StringRenderable;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.translate.function.extract.LocaleExtractor;
import org.jtwig.translate.function.extract.ReplacementsExtractor;
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
        Renderable renderable = request.getEnvironment().getRenderEnvironment().getRenderNodeService().render(request, node.getContent());
        String message = renderable
                .appendTo(new StringBuilderRenderResult())
                .content().trim();

        Locale locale;
        if (node.getLocaleExpression().isPresent()) {
            Object calculatedValue = request.getEnvironment().getRenderEnvironment().getCalculateExpressionService().calculate(request, node.getLocaleExpression().get());
            Optional<Locale> optional = localeExtractor.extract(request.getEnvironment(), calculatedValue);

            if (optional.isPresent()) {
                locale = optional.get();
            } else {
                throw new CalculationException(ErrorMessageFormatter.errorMessage(node.getPosition(), String.format("Unable to convert '%s' to locale", calculatedValue)));
            }
        } else {
            locale = TranslateConfiguration.currentLocaleSupplier(request.getEnvironment()).get();
        }

        Collection<MessageDecorator> decorators = new ArrayList<>();

        if (node.getWithExpression().isPresent()) {
            Object calculatedValue = request.getEnvironment().getRenderEnvironment().getCalculateExpressionService().calculate(request, node.getWithExpression().get());
            Optional<Collection<ReplacementMessageDecorator.Replacement>> optional = replacementsExtractor.extract(request.getEnvironment(), calculatedValue);
            if (optional.isPresent()) {
                decorators.add(new ReplacementMessageDecorator(optional.get()));
            } else {
                throw new CalculationException(ErrorMessageFormatter.errorMessage(node.getPosition(), String.format("Unable to convert '%s' to replacements", calculatedValue)));
            }
        }
        decorators.add(new ExpressionMessageDecorator(fromContextValue(request.getRenderContext().getValueContext().getCurrent(), request.getEnvironment().getValueEnvironment())));

        CompositeMessageDecorator messageDecorator = new CompositeMessageDecorator(decorators);
        return new StringRenderable(TranslateConfiguration.messageResolver(request.getEnvironment())
                .resolve(locale, message, messageDecorator)
                .or(defaultMessage(message, messageDecorator)), request.getRenderContext().getEscapeEngineContext().getCurrent());
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
