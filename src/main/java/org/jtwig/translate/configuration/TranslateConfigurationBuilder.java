package org.jtwig.translate.configuration;

import com.google.common.base.Supplier;
import org.apache.commons.lang3.builder.Builder;
import org.jtwig.i18n.locale.LocaleResolver;

import java.util.Locale;

public class TranslateConfigurationBuilder implements Builder<TranslateConfiguration> {
    private final AndMessageResolverConfigurationBuilder messageResolverConfigurationBuilder;
    private Supplier<Locale> localeSupplier;
    private LocaleResolver localeResolver;

    public TranslateConfigurationBuilder() {
        this.messageResolverConfigurationBuilder = new AndMessageResolverConfigurationBuilder(this);
    }

    public TranslateConfigurationBuilder(TranslateConfiguration prototype) {
        this.localeResolver = prototype.getLocaleResolver();
        this.localeSupplier = prototype.getLocaleSupplier();
        this.messageResolverConfigurationBuilder = new AndMessageResolverConfigurationBuilder(prototype.getConfiguration(), this);
    }

    public TranslateConfigurationBuilder withCurrentLocaleSupplier (Supplier<Locale> supplier) {
        this.localeSupplier = supplier;
        return this;
    }

    public TranslateConfigurationBuilder withStringLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
        return this;
    }

    public AndMessageResolverConfigurationBuilder messages () {
        return messageResolverConfigurationBuilder;
    }

    @Override
    public TranslateConfiguration build() {
        return new TranslateConfiguration(messageResolverConfigurationBuilder.build(), localeSupplier, localeResolver);
    }
}
