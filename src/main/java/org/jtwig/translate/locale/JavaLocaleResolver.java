package org.jtwig.translate.locale;

import java.util.Locale;

public class JavaLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolve(String localeIdentifier) {
        return Locale.forLanguageTag(localeIdentifier);
    }
}
