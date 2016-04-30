package org.jtwig.translate.message.source.cache.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Locale;

public class LocaleAndMessage {
    private final Locale locale;
    private final String message;

    public LocaleAndMessage(Locale locale, String message) {
        this.locale = locale;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LocaleAndMessage that = (LocaleAndMessage) o;

        return new EqualsBuilder()
                .append(locale, that.locale)
                .append(message, that.message)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(locale)
                .append(message)
                .toHashCode();
    }
}
