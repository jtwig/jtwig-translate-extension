package org.jtwig.translate.decorator;

import com.google.common.base.Optional;
import org.jtwig.i18n.decorate.MessageDecorator;
import org.jtwig.model.position.Position;
import org.jtwig.plural.PluralOptions;
import org.jtwig.util.ErrorMessageFormatter;

public class PluralSelector implements MessageDecorator {
    private final Position position;
    private final int value;

    public PluralSelector(Position position, int value) {
        this.position = position;
        this.value = value;
    }

    @Override
    public String decorate(String translated) {
        Optional<String> optional = PluralOptions.parse(translated).select(value);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new IllegalArgumentException(ErrorMessageFormatter.errorMessage(position, String.format("Unable to select option for %d from '%s'", value, translated)));
        }
    }
}
