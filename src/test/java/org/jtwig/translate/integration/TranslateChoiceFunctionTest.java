package org.jtwig.translate.integration;

import org.jtwig.i18n.source.message.MapMessageSource;
import org.jtwig.translate.TranslateExtension;
import org.jtwig.translate.configuration.StaticLocaleSupplier;
import org.jtwig.translate.configuration.TranslateConfigurationBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.jtwig.JtwigModel.newModel;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import static org.jtwig.environment.EnvironmentConfigurationBuilder.configuration;

public class TranslateChoiceFunctionTest {

    @Test
    public void translateSimple() throws Exception {
        String result =
                inlineTemplate("{{ 'Hi' | translateChoice(1) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder()
                                .build())).build())
                        .render(newModel());

        assertThat(result, is("Hi"));
    }

    @Test
    public void translateWithChoiceOne() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(1) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder()
                                .build())).build())
                        .render(newModel());

        assertThat(result, is("One"));
    }

    @Test
    public void translateWithChoiceZero() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(0) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder()
                                .build())).build())
                        .render(newModel());

        assertThat(result, is("Zero"));
    }

    @Test
    public void translateWithChoiceMulti() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(2) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder()
                                .build())).build())
                        .render(newModel());

        assertThat(result, is("Multi"));
    }

    @Test
    public void translateWithTranslation() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(1) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder()
                                .messages().withMessageSource(Locale.ITALY, singleMessageSource("{0} Zero | {1} One | ]1, Inf[ Multi", "{0} Zero | [1, Inf[ Varios")).and()
                                .withCurrentLocaleSupplier(new StaticLocaleSupplier(Locale.ITALY))
                                .build()))
                        .build())
                        .render(newModel());

        assertThat(result, is("Varios"));
    }

    @Test
    public void translateWithTranslationAndLocale() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(3, 'it-IT') }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder()
                                .messages().withMessageSource(Locale.ITALY, singleMessageSource("{0} Zero | {1} One | ]1, Inf[ Multi", "{0} Zero | [1, Inf[ Varios")).and()
                                .build()))
                        .build())
                        .render(newModel());

        assertThat(result, is("Varios"));
    }

    @Test
    public void translateWithTranslationAndLocaleAndReplacements() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(3, {'%name%': 'Joao'},'it-IT') }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder()
                                .messages().withMessageSource(Locale.ITALY, singleMessageSource("{0} Zero | {1} One | ]1, Inf[ Multi", "{0} Zero | [1, Inf[ Varios %name%")).and()
                                .build()))
                        .build())
                        .render(newModel());

        assertThat(result, is("Varios Joao"));
    }

    private MapMessageSource singleMessageSource(final String key, final String value) {
        return new MapMessageSource(new HashMap<String, String>() {{ put(key, value); }});
    }
}
