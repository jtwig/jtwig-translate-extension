package org.jtwig.translate.configuration;

import com.google.common.base.Supplier;
import org.apache.commons.lang3.builder.Builder;
import org.jtwig.translate.locale.LocaleResolver;
import org.jtwig.translate.message.source.MessageSourceFactory;

import java.util.Locale;

public class TranslateConfigurationBuilder implements Builder<TranslateConfiguration> {
    private MessageSourceFactory messageSourceFactory;
    private Supplier<Locale> localeSupplier;
    private LocaleResolver localeResolver;

    public TranslateConfigurationBuilder() {}

    public TranslateConfigurationBuilder(TranslateConfiguration prototype) {
        this.localeResolver = prototype.getLocaleResolver();
        this.localeSupplier = prototype.getLocaleSupplier();
        this.messageSourceFactory = prototype.getMessageSourceFactory();
    }

    public TranslateConfigurationBuilder withMessageSourceFactory(MessageSourceFactory messageSourceFactory) {
        this.messageSourceFactory = messageSourceFactory;
        return this;
    }

    public TranslateConfigurationBuilder withCurrentLocaleSupplier (Supplier<Locale> supplier) {
        this.localeSupplier = supplier;
        return this;
    }

    public TranslateConfigurationBuilder withStringLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
        return this;
    }

    public TranslateConfiguration build() {
        return new TranslateConfiguration(messageSourceFactory, localeSupplier, localeResolver);
    }
}
