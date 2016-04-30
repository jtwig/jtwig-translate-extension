package org.jtwig.translate;

import org.jtwig.environment.Environment;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.extension.Extension;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.translate.environment.TranslateEnvironment;
import org.jtwig.translate.function.TranslateChoiceFunction;
import org.jtwig.translate.function.TranslateFunction;
import org.jtwig.translate.function.Translator;
import org.jtwig.translate.function.extract.LocaleExtractor;
import org.jtwig.translate.function.extract.LocaleOrReplacementsExtractor;
import org.jtwig.translate.function.extract.ReplacementsExtractor;
import org.jtwig.translate.function.extract.TranslateParameterExtractor;
import org.jtwig.translate.message.DefaultMessageResolver;
import org.jtwig.translate.message.source.DefaultMessageSource;
import org.jtwig.translate.node.TranslateNode;
import org.jtwig.translate.parser.TranslateAddonParserProvider;
import org.jtwig.translate.render.node.TranslateNodeRender;

public class TranslateExtension implements Extension {
    public static final String TRANSLATE_EXTENSION_ENVIRONMENT = "translate-extension-environment";

    public static TranslateEnvironment enviroment(Environment environment) {
        return environment.parameter(TRANSLATE_EXTENSION_ENVIRONMENT);
    }

    private final TranslateConfiguration configuration;

    public TranslateExtension(TranslateConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void configure(EnvironmentConfigurationBuilder environmentConfigurationBuilder) {
        DefaultMessageSource messageSource = new DefaultMessageSource(configuration.getMessageSourceFactory());
        TranslateEnvironment translateEnvironment = new TranslateEnvironment(
                new DefaultMessageResolver(messageSource),
                configuration.getLocaleSupplier(),
                configuration.getLocaleResolver()
        );

        Translator translator = new Translator();
        LocaleExtractor localeExtractor = new LocaleExtractor();
        ReplacementsExtractor replacementsExtractor = new ReplacementsExtractor();
        LocaleOrReplacementsExtractor localeOrReplacementsExtractor = new LocaleOrReplacementsExtractor(localeExtractor, replacementsExtractor);

        TranslateParameterExtractor parameterExtractor = new TranslateParameterExtractor(
                localeExtractor, replacementsExtractor,
                localeOrReplacementsExtractor
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
                    .add(new TranslateFunction(parameterExtractor, translator))
                    .add(new TranslateChoiceFunction(parameterExtractor, translator))
                .and()
                .parameters()
                    .add(TRANSLATE_EXTENSION_ENVIRONMENT, translateEnvironment)
                .and()
                .initializers()
                    .add(messageSource)
                .and()
        ;
    }
}
