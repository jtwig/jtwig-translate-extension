package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.translate.configuration.TranslateConfiguration;

import java.util.Locale;

public class LocaleExtractor {
    public Optional<Locale> extract (Environment environment, Object input) {
        if (input instanceof Locale) {
            return Optional.of((Locale) input);
        } else if (input instanceof String) {
            Locale resolve = TranslateConfiguration.localeResolver(environment).resolve((String) input);
            return Optional.of(resolve);
        }

        return Optional.absent();
    }
}
