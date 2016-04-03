package org.jtwig.translate;

import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.extension.Extension;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.translate.function.TranslateChoiceFunction;
import org.jtwig.translate.function.TranslateFunction;
import org.jtwig.translate.function.extract.LocaleExtractor;
import org.jtwig.translate.function.extract.ReplacementsExtractor;
import org.jtwig.translate.node.TranslateNode;
import org.jtwig.translate.parser.TranslateAddonParserProvider;
import org.jtwig.translate.render.node.TranslateNodeRender;

public class TranslateExtension implements Extension {
    private final TranslateConfiguration configuration;

    public TranslateExtension(TranslateConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void configure(EnvironmentConfigurationBuilder environmentConfigurationBuilder) {
        configuration.registerParameters(environmentConfigurationBuilder);
        LocaleExtractor localeExtractor = new LocaleExtractor();
        ReplacementsExtractor replacementsExtractor = new ReplacementsExtractor();

        environmentConfigurationBuilder
                .parser()
                    .withAddonParserProvider(new TranslateAddonParserProvider())
                    .and()
                .render()
                    .withRender(TranslateNode.class, new TranslateNodeRender(localeExtractor, replacementsExtractor))
                    .and()
                .functions()
                    .withFunction(new TranslateFunction(localeExtractor, replacementsExtractor))
                    .withFunction(new TranslateChoiceFunction(localeExtractor, replacementsExtractor))
        ;
    }
}
