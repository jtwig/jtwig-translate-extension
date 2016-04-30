package org.jtwig.translate.configuration;

import com.google.common.base.Supplier;
import org.jtwig.translate.locale.LocaleResolver;
import org.jtwig.translate.message.source.MessageSourceFactory;

import java.util.Locale;

public class TranslateConfiguration {
    private final MessageSourceFactory messageSourceFactory;
    private final Supplier<Locale> localeSupplier;
    private final LocaleResolver localeResolver;

    public TranslateConfiguration(MessageSourceFactory messageSourceFactory, Supplier<Locale> localeSupplier, LocaleResolver localeResolver) {
        this.messageSourceFactory = messageSourceFactory;
        this.localeSupplier = localeSupplier;
        this.localeResolver = localeResolver;
    }

    public MessageSourceFactory getMessageSourceFactory() {
        return messageSourceFactory;
    }

    public Supplier<Locale> getLocaleSupplier() {
        return localeSupplier;
    }

    public LocaleResolver getLocaleResolver() {
        return localeResolver;
    }
}
