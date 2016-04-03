package org.jtwig.translate.integration;

import org.jtwig.i18n.source.message.MapMessageSource;
import org.jtwig.translate.TranslateExtension;
import org.jtwig.translate.configuration.DefaultTranslateConfiguration;
import org.jtwig.translate.configuration.StaticLocaleSupplier;
import org.jtwig.translate.configuration.TranslateConfigurationBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.jtwig.JtwigModel.newModel;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import static org.jtwig.environment.EnvironmentConfigurationBuilder.configuration;

public class TranslateChoiceFunctionTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void translateSimple() throws Exception {
        String result =
                inlineTemplate("{{ 'Hi' | translateChoice(1) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .build())).build())
                        .render(newModel());

        assertThat(result, is("Hi"));
    }

    @Test
    public void translateSimpleInvalidArgument() throws Exception {
        expectedException.expectMessage(containsString("Expecting number as second argument but got 'a'"));

        inlineTemplate("{{ 'Hi' | translateChoice('a') }}", configuration()
                .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).build())
                .render(newModel());
    }

    @Test
    public void translateSimpleInvalid3rdArgument() throws Exception {
        expectedException.expectMessage(containsString("Expecting map or locale as third argument, but got '1'"));

        inlineTemplate("{{ 'Hi' | translateChoice(0, 1) }}", configuration()
                .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).build())
                .render(newModel());
    }

    @Test
    public void translateSimpleInvalid3rdArgumentIn4() throws Exception {
        expectedException.expectMessage(containsString("Expecting map as third argument, but got '1'"));

        inlineTemplate("{{ 'Hi' | translateChoice(0, 1, 2) }}", configuration()
                .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).build())
                .render(newModel());
    }

    @Test
    public void translateSimpleInvalid4thArgumentIn4() throws Exception {
        expectedException.expectMessage(containsString("Expecting locale as fourth argument, but got '2'"));

        inlineTemplate("{{ 'Hi' | translateChoice(0, {}, 2) }}", configuration()
                .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).build())
                .render(newModel());
    }

    @Test
    public void translateSimpleWithMap() throws Exception {
        String result = inlineTemplate("{{ 'Hi %name%' | translateChoice(0, {'%name%':'Jtwig'}) }}", configuration()
                .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).build())
                .render(newModel());

        assertThat(result, is("Hi Jtwig"));
    }

    @Test
    public void translateWithChoiceOne() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(1) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .build())).build())
                        .render(newModel());

        assertThat(result, is("One"));
    }

    @Test
    public void translateWithChoiceZero() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(0) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .build())).build())
                        .render(newModel());

        assertThat(result, is("Zero"));
    }

    @Test
    public void translateWithChoiceMulti() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(2) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .build())).build())
                        .render(newModel());

        assertThat(result, is("Multi"));
    }

    @Test
    public void translateWithTranslation() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(1) }}", configuration()
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
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
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
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
                        .withExtension(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .messages().withMessageSource(Locale.ITALY, singleMessageSource("{0} Zero | {1} One | ]1, Inf[ Multi", "{0} Zero | [1, Inf[ Varios %name%")).and()
                                .build()))
                        .build())
                        .render(newModel());

        assertThat(result, is("Varios Joao"));
    }

    private MapMessageSource singleMessageSource(final String key, final String value) {
        return new MapMessageSource(new HashMap<String, String>() {{
            put(key, value);
        }});
    }
}
