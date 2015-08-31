package org.jtwig.translate.configuration;

import org.jtwig.i18n.MessageResolverConfiguration;
import org.jtwig.i18n.locale.JavaLocaleResolver;
import org.jtwig.i18n.source.LocalizedMessageSource;

import java.util.Collections;
import java.util.Locale;

public class DefaultTranslateConfiguration extends TranslateConfiguration {
    public DefaultTranslateConfiguration() {
        super(new MessageResolverConfiguration(Collections.<LocalizedMessageSource>emptyList()), new StaticLocaleSupplier(Locale.ENGLISH), new JavaLocaleResolver());
    }
}
