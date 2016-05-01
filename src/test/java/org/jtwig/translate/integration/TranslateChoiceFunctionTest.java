package org.jtwig.translate.integration;

import org.jtwig.translate.TranslateExtension;
import org.jtwig.translate.configuration.DefaultTranslateConfiguration;
import org.jtwig.translate.configuration.StaticLocaleSupplier;
import org.jtwig.translate.configuration.TranslateConfigurationBuilder;
import org.jtwig.translate.message.source.InMemoryMessageSource;
import org.jtwig.translate.message.source.factory.MessageSourceFactory;
import org.jtwig.translate.message.source.factory.SingletonMessageSourceFactory;
import org.jtwig.translate.message.source.localized.resource.InMemoryLocalizedMessageResource;
import org.jtwig.translate.message.source.localized.resource.LocalizedMessageResource;
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
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .build())).and().build())
                        .render(newModel());

        assertThat(result, is("Hi"));
    }

    @Test
    public void translateSimpleInvalidArgument() throws Exception {
        expectedException.expectMessage(containsString("Expecting number as second argument but got 'a'"));

        inlineTemplate("{{ 'Hi' | translateChoice('a') }}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).and().build())
                .render(newModel());
    }

    @Test
    public void translateSimpleInvalid3rdArgument() throws Exception {
        expectedException.expectMessage(containsString("Expecting map or locale, but got '1'"));

        inlineTemplate("{{ 'Hi' | translateChoice(0, 1) }}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).and().build())
                .render(newModel());
    }

    @Test
    public void translateSimpleInvalid3rdArgumentIn4() throws Exception {
        expectedException.expectMessage(containsString("Expecting map, but got '1'"));

        inlineTemplate("{{ 'Hi' | translateChoice(0, 1, 2) }}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).and().build())
                .render(newModel());
    }

    @Test
    public void translateSimpleInvalid4thArgumentIn4() throws Exception {
        expectedException.expectMessage(containsString("Expecting locale, but got '2'"));

        inlineTemplate("{{ 'Hi' | translateChoice(0, {}, 2) }}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).and().build())
                .render(newModel());
    }

    @Test
    public void translateSimpleWithMap() throws Exception {
        String result = inlineTemplate("{{ 'Hi %name%' | translateChoice(0, {'%name%':'Jtwig'}) }}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).and().build())
                .render(newModel());

        assertThat(result, is("Hi Jtwig"));
    }

    @Test
    public void translateWithChoiceOne() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(1) }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .build())).and().build())
                        .render(newModel());

        assertThat(result, is("One"));
    }

    @Test
    public void translateWithChoiceZero() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(0) }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .build())).and().build())
                        .render(newModel());

        assertThat(result, is("Zero"));
    }

    @Test
    public void translateWithChoiceMulti() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(2) }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .build())).and().build())
                        .render(newModel());

        assertThat(result, is("Multi"));
    }

    @Test
    public void translateWithTranslation() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(1) }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(singleEntry(Locale.ITALY, "{0} Zero | {1} One | ]1, Inf[ Multi", "{0} Zero | [1, Inf[ Varios"))
                                .withCurrentLocaleSupplier(new StaticLocaleSupplier(Locale.ITALY))
                                .build())).and()
                        .build())
                        .render(newModel());

        assertThat(result, is("Varios"));
    }

    @Test
    public void translateWithTranslationAndLocale() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(3, 'it-IT') }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(singleEntry(Locale.ITALY, "{0} Zero | {1} One | ]1, Inf[ Multi", "{0} Zero | [1, Inf[ Varios"))
                                .build())).and()
                        .build())
                        .render(newModel());

        assertThat(result, is("Varios"));
    }

    @Test
    public void translateWithTranslationAndLocaleAndReplacements() throws Exception {
        String result =
                inlineTemplate("{{ '{0} Zero | {1} One | ]1, Inf[ Multi' | translateChoice(3, {'%name%': 'Joao'},'it-IT') }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(singleEntry(Locale.ITALY, "{0} Zero | {1} One | ]1, Inf[ Multi", "{0} Zero | [1, Inf[ Varios %name%"))
                                .build())).and()
                        .build())
                        .render(newModel());

        assertThat(result, is("Varios Joao"));
    }

    private MessageSourceFactory singleEntry(final Locale locale, final String origin, final String target) {
        return new SingletonMessageSourceFactory(new InMemoryMessageSource(
                new HashMap<Locale, LocalizedMessageResource>() {{
                    put(locale, new InMemoryLocalizedMessageResource(locale, new HashMap<String, String>() {{
                        put(origin, target);
                    }}));
                }}
        ));
    }
}
