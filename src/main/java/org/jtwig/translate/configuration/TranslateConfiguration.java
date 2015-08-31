package org.jtwig.translate.configuration;

import com.google.common.base.Supplier;
import org.jtwig.environment.Environment;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.i18n.MessageResolver;
import org.jtwig.i18n.MessageResolverConfiguration;
import org.jtwig.i18n.MessageResolverFactory;
import org.jtwig.i18n.locale.LocaleResolver;

import java.util.Locale;

public class TranslateConfiguration {
    private static final String MESSAGE_RESOLVER = "messageResolver";
    private static final String LOCALE_SUPPLIER = "currentLocaleSupplier";
    private static final String LOCALE_RESOLVER = "localeResolver";

    public static MessageResolver messageResolver (Environment environment) {
        return environment.parameter(MESSAGE_RESOLVER);
    }
    public static Supplier<Locale> currentLocaleSupplier (Environment environment) {
        return environment.parameter(LOCALE_SUPPLIER);
    }

    public static LocaleResolver localeResolver(Environment environment) {
        return environment.parameter(LOCALE_RESOLVER);
    }

    private final MessageResolverConfiguration configuration;
    private final Supplier<Locale> localeSupplier;
    private final LocaleResolver localeResolver;

    public TranslateConfiguration(MessageResolverConfiguration configuration, Supplier<Locale> localeSupplier, LocaleResolver localeResolver) {
        this.configuration = configuration;
        this.localeSupplier = localeSupplier;
        this.localeResolver = localeResolver;
    }

    public EnvironmentConfigurationBuilder registerParameters(EnvironmentConfigurationBuilder environmentConfigurationBuilder) {
        MessageResolver messageResolver = new MessageResolverFactory().create(configuration);
        environmentConfigurationBuilder.withParameter(MESSAGE_RESOLVER, messageResolver);
        environmentConfigurationBuilder.withParameter(LOCALE_SUPPLIER, localeSupplier);
        environmentConfigurationBuilder.withParameter(LOCALE_RESOLVER, localeResolver);
        return environmentConfigurationBuilder;
    }

    public MessageResolverConfiguration getConfiguration() {
        return configuration;
    }

    public Supplier<Locale> getLocaleSupplier() {
        return localeSupplier;
    }

    public LocaleResolver getLocaleResolver() {
        return localeResolver;
    }
}
