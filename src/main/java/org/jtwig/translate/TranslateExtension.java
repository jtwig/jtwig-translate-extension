package org.jtwig.translate;

import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.extension.Extension;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.translate.function.TranslateChoiceFunction;
import org.jtwig.translate.function.TranslateFunction;
import org.jtwig.translate.function.extract.LocaleExtractor;
import org.jtwig.translate.function.extract.LocaleOrReplacementsExtractor;
import org.jtwig.translate.function.extract.ReplacementsExtractor;
import org.jtwig.translate.function.extract.TranslateParameterExtractor;
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

        TranslateParameterExtractor parameterExtractor = new TranslateParameterExtractor(
                localeExtractor, replacementsExtractor,
                new LocaleOrReplacementsExtractor(localeExtractor, replacementsExtractor)
        );
        environmentConfigurationBuilder
                .parser()
                    .addonParserProviders().add(new TranslateAddonParserProvider()).and()
                    .and()
                .render()
                    .nodeRenders().add(TranslateNode.class, new TranslateNodeRender(localeExtractor, replacementsExtractor))
                    .and()
                    .and()
                .functions()
                    .add(new TranslateFunction(localeExtractor, replacementsExtractor))
                    .add(new TranslateChoiceFunction(parameterExtractor))
        ;
    }
}
