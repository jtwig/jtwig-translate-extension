package org.jtwig.translate.configuration;

import com.google.common.base.Supplier;
import org.jtwig.translate.locale.LocaleResolver;
import org.jtwig.translate.message.source.factory.MessageSourceFactory;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

public class TranslateConfigurationBuilderTest {
    @Test
    public void configurationTest() throws Exception {
        LocaleResolver localeResolver = mock(LocaleResolver.class);
        MessageSourceFactory messageSourceFactory = mock(MessageSourceFactory.class);
        Supplier<Locale> currentLocaleSupplier = mock(Supplier.class);

        TranslateConfiguration configuration = TranslateConfigurationBuilder
                .translateConfiguration()
                    .withMessageSourceFactory(messageSourceFactory)
                    .withStringLocaleResolver(localeResolver)
                    .withCurrentLocaleSupplier(currentLocaleSupplier)
                .build();


        assertThat(configuration.getMessageSourceFactory(), is(messageSourceFactory));
        assertThat(configuration.getLocaleResolver(), is(localeResolver));
        assertThat(configuration.getLocaleSupplier(), is(currentLocaleSupplier));
    }

    @Test
    public void emptyConfiguration() throws Exception {

        TranslateConfiguration configuration = new TranslateConfigurationBuilder().build();

        assertThat(configuration.getMessageSourceFactory(), is(nullValue()));
        assertThat(configuration.getLocaleResolver(), is(nullValue()));
        assertThat(configuration.getLocaleSupplier(), is(nullValue()));
    }
}