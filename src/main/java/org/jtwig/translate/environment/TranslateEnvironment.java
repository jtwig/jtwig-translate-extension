package org.jtwig.translate.environment;

import com.google.common.base.Supplier;
import org.jtwig.translate.locale.LocaleResolver;
import org.jtwig.translate.message.MessageResolver;

import java.util.Locale;

public class TranslateEnvironment {
    private final MessageResolver messageResolver;
    private final Supplier<Locale> localeSupplier;
    private final LocaleResolver localeResolver;

    public TranslateEnvironment(MessageResolver messageResolver, Supplier<Locale> localeSupplier, LocaleResolver localeResolver) {
        this.messageResolver = messageResolver;
        this.localeSupplier = localeSupplier;
        this.localeResolver = localeResolver;
    }

    public MessageResolver getMessageResolver() {
        return messageResolver;
    }

    public Supplier<Locale> getLocaleSupplier() {
        return localeSupplier;
    }

    public LocaleResolver getLocaleResolver() {
        return localeResolver;
    }
}
