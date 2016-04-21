package org.jtwig.translate.integration;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
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
import static org.jtwig.environment.EnvironmentConfigurationBuilder.configuration;

public class TranslateNodeTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void simpleTranslate() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{% trans %}Hi{% endtrans %}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .build())).and()
                .build())
                .render(JtwigModel.newModel());

        assertThat(result, is("Hi"));
    }

    @Test
    public void simpleTranslateWithTranslation() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{% trans %}Hi{% endtrans %}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .messages().withMessageSource(Locale.ITALY, singleMessageResource("Hi", "Ciao")).and()
                        .withCurrentLocaleSupplier(new StaticLocaleSupplier(Locale.ITALY))
                        .build())).and()
                .build())
                .render(JtwigModel.newModel());

        assertThat(result, is("Ciao"));
    }

    @Test
    public void simpleTranslateWithTranslationInto() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{% trans into 'it-IT' %}Hi{% endtrans %}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .messages().withMessageSource(Locale.ITALY, singleMessageResource("Hi", "Ciao")).and()
                        .withCurrentLocaleSupplier(new StaticLocaleSupplier(Locale.ENGLISH))
                        .build())).and()
                .build())
                .render(JtwigModel.newModel());

        assertThat(result, is("Ciao"));
    }

    @Test
    public void simpleTranslateWithTranslationIntoInvalid() throws Exception {
        expectedException.expectMessage(containsString("Unable to convert '1' to locale"));

        JtwigTemplate.inlineTemplate("{% trans into 1 %}Hi{% endtrans %}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .messages().withMessageSource(Locale.ITALY, singleMessageResource("Hi", "Ciao")).and()
                        .withCurrentLocaleSupplier(new StaticLocaleSupplier(Locale.ENGLISH))
                        .build())).and()
                .build())
                .render(JtwigModel.newModel());
    }

    @Test
    public void simpleTranslateWithTranslationWithParameter() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{% trans with { '%name%': 'Joao' } %}Hi %name%{% endtrans %}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .messages().withMessageSource(Locale.ITALY, singleMessageResource("Hi %name%", "Ciao %name%")).and()
                        .withCurrentLocaleSupplier(new StaticLocaleSupplier(Locale.ITALY))
                        .build())).and()
                .build())
                .render(JtwigModel.newModel());

        assertThat(result, is("Ciao Joao"));
    }

    @Test
    public void simpleTranslateWithTranslationWithParameterInvalid() throws Exception {
        expectedException.expectMessage(containsString("Unable to convert '1' to replacements"));

        JtwigTemplate.inlineTemplate("{% trans with 1 %}Hi %name%{% endtrans %}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .messages().withMessageSource(Locale.ITALY, singleMessageResource("Hi %name%", "Ciao %name%")).and()
                        .withCurrentLocaleSupplier(new StaticLocaleSupplier(Locale.ITALY))
                        .build())).and()
                .build())
                .render(JtwigModel.newModel());
    }

    @Test
    public void translateWithTranslationWithContextVariable() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{% trans %}Hi %name%{% endtrans %}", configuration()
                .extensions().add(new TranslateExtension(new TranslateConfigurationBuilder(new DefaultTranslateConfiguration())
                        .messages().withMessageSource(Locale.ITALY, singleMessageResource("Hi %name%", "Ciao %name%")).and()
                        .withCurrentLocaleSupplier(new StaticLocaleSupplier(Locale.ITALY))
                        .build())).and()
                .build())
                .render(JtwigModel.newModel().with("name", "World"));

        assertThat(result, is("Ciao World"));
    }

    private MapMessageSource singleMessageResource(final String key, final String value) {
        return new MapMessageSource(new HashMap<String, String>() {{ put(key, value); }});
    }
}
