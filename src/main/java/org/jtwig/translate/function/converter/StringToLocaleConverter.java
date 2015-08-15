package org.jtwig.translate.function.converter;

import com.google.common.base.Optional;
import org.jtwig.context.RenderContextHolder;
import org.jtwig.environment.Environment;
import org.jtwig.reflection.model.Value;
import org.jtwig.translate.configuration.TranslateConfiguration;
import org.jtwig.value.converter.Converter;

import java.util.Locale;

public class StringToLocaleConverter implements Converter {
    @Override
    public Optional<Value> convert(Object value, Class type) {
        if (type.equals(Locale.class)) {
            if (value instanceof String) {
                Locale locale = TranslateConfiguration.localeResolver(getEnvironment()).resolve((String) value);
                return Optional.of(new Value(locale));
            }
        }
        return Optional.absent();
    }

    private Environment getEnvironment() {
        return RenderContextHolder.get().environment();
    }
}
