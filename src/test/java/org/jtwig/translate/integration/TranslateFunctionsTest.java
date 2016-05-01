package org.jtwig.translate.integration;

import org.jtwig.translate.TranslateExtension;
import org.jtwig.translate.configuration.DefaultTranslateConfiguration;
import org.jtwig.translate.configuration.StaticLocaleSupplier;
import org.jtwig.translate.configuration.TranslateConfigurationBuilder;
import org.jtwig.translate.locale.JavaLocaleResolver;
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

public class TranslateFunctionsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void translateSimple() throws Exception {
        String result =
                inlineTemplate("{{ 'Hi' | translate }}", configuration()
                        .extensions().add(new TranslateExtension(new DefaultTranslateConfiguration())).and()
                        .build())
                        .render(newModel());

        assertThat(result, is("Hi"));
    }

    @Test
    public void translateWithTranslation() throws Exception {
        Locale current = Locale.ITALIAN;

        String result =
                inlineTemplate("{{ 'Hi' | translate }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(singleEntry(current, "Hi", "Ciao"))
                                .withCurrentLocaleSupplier(new StaticLocaleSupplier(current))
                                .build())).and()
                        .build()
                ).render(newModel());

        assertThat(result, is("Ciao"));
    }

    @Test
    public void translateWithParameters() throws Exception {
        Locale current = Locale.ITALIAN;

        String result =
                inlineTemplate("{{ 'Hi %name%' | translate({ '%name%': 'Joao' }) }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(singleEntry(current, "Hi %name%", "Ciao %name%"))
                                .withCurrentLocaleSupplier(new StaticLocaleSupplier(current))
                                .build())).and().build())
                        .render(newModel());

        assertThat(result, is("Ciao Joao"));
    }

    @Test
    public void translateWithInvalidSecondArgument() throws Exception {
        expectedException.expectMessage(containsString("Expecting map or locale, but got '1'"));

        inlineTemplate("{{ 'Hi %name%' | translate(1) }}", configuration()
                .extensions().add(new TranslateExtension(new DefaultTranslateConfiguration())).and()
                .build())
                .render(newModel());
    }

    @Test
    public void translateWithParametersInAnotherLocale() throws Exception {
        Locale current = Locale.ITALIAN;

        String result =
                inlineTemplate("{{ 'Hi %name%' | translate({ '%name%': 'Joao' }, 'pt') }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(singleEntry(new Locale("pt"), "Hi %name%", "Ola %name%"))
                                .withCurrentLocaleSupplier(new StaticLocaleSupplier(current))
                                .build())).and()
                        .build())
                        .render(newModel());

        assertThat(result, is("Ola Joao"));
    }

    @Test
    public void translateWithParametersInAnotherLocaleWithWrong2ndParameter() throws Exception {
        expectedException.expectMessage(containsString("Expecting map, but got '1'"));

        inlineTemplate("{{ 'Hi %name%' | translate(1, 'pt') }}", configuration()
                .extensions().add(new TranslateExtension(new DefaultTranslateConfiguration())).and()
                .build())
                .render(newModel());
    }

    @Test
    public void translateWithParametersInAnotherLocaleWithWrong3rdParameter() throws Exception {
        expectedException.expectMessage(containsString("Expecting locale, but got '1'"));

        inlineTemplate("{{ 'Hi %name%' | translate({}, 1) }}", configuration()
                .extensions().add(new TranslateExtension(new DefaultTranslateConfiguration())).and()
                .build())
                .render(newModel());
    }

    @Test
    public void translateInAnotherLocale() throws Exception {
        Locale locale = new JavaLocaleResolver().resolve("pt");
        Locale current = Locale.ITALIAN;

        String result =
                inlineTemplate("{{ 'Hello' | translate('pt') }}", configuration()
                        .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                                .withMessageSourceFactory(singleEntry(locale, "Hello", "Ola"))
                                .withCurrentLocaleSupplier(new StaticLocaleSupplier(current))
                                .build())).and()
                        .build())
                        .render(newModel());

        assertThat(result, is("Ola"));
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
