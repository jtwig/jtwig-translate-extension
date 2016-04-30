package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.translate.message.decorate.ReplacementMessageDecorator;
import org.jtwig.value.WrappedCollection;
import org.jtwig.value.convert.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ReplacementsExtractor {
    public Optional<Collection<ReplacementMessageDecorator.Replacement>> extract (Environment environment, Object input) {
        Converter.Result<WrappedCollection> convert = environment.getValueEnvironment().getCollectionConverter().convert(input);
        if (convert.isDefined()) {
            Collection<ReplacementMessageDecorator.Replacement> result = new ArrayList<>();
            for (Map.Entry<String, Object> entry : convert.get()) {
                if (entry.getKey() != null) {
                    String stringValue = environment.getValueEnvironment().getStringConverter().convert(entry.getValue());
                    result.add(new ReplacementMessageDecorator.Replacement(entry.getKey(), stringValue));
                }
            }
            return Optional.of(result);
        } else {
            return Optional.absent();
        }
    }
}
