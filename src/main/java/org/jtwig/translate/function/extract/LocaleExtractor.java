package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;

import java.util.Locale;

import static org.jtwig.translate.TranslateExtension.enviroment;

public class LocaleExtractor {
    public Optional<Locale> extract (Environment environment, Object input) {
        if (input instanceof Locale) {
            return Optional.of((Locale) input);
        } else if (input instanceof String) {
            Locale resolve = enviroment(environment).getLocaleResolver().resolve((String) input);
            return Optional.of(resolve);
        }

        return Optional.absent();
    }
}
