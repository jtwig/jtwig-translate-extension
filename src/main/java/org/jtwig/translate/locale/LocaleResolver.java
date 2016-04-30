package org.jtwig.translate.locale;

import java.util.Locale;

public interface LocaleResolver {
    Locale resolve (String localeIdentifier);
}
