package org.jtwig.translate.decorator;

import org.jtwig.i18n.decorate.MessageDecorator;
import org.jtwig.plural.PluralOptions;
import org.jtwig.util.OptionalUtils;

public class PluralSelector implements MessageDecorator {
    private final int value;

    public PluralSelector(int value) {
        this.value = value;
    }

    @Override
    public String decorate(String translated) {
        return PluralOptions.parse(translated).select(value)
                .or(OptionalUtils.<String>throwException(String.format("Unable to select option for %d from '%s'", value, translated)));
    }
}
