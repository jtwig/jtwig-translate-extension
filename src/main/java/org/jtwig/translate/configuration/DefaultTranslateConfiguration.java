package org.jtwig.translate.configuration;

import org.jtwig.translate.locale.JavaLocaleResolver;
import org.jtwig.translate.message.source.EmptyMessageSource;
import org.jtwig.translate.message.source.SingletonMessageSourceFactory;

import java.util.Locale;

public class DefaultTranslateConfiguration extends TranslateConfiguration {
    public DefaultTranslateConfiguration() {
        super(new SingletonMessageSourceFactory(EmptyMessageSource.emptyMessageSource()),
                new StaticLocaleSupplier(Locale.ENGLISH),
                new JavaLocaleResolver());
    }
}
