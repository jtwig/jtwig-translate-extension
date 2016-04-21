package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;

import java.util.Collection;
import java.util.Locale;

public class LocaleOrReplacementsExtractor {
    private final LocaleExtractor localeExtractor;
    private final ReplacementsExtractor replacementsExtractor;

    public LocaleOrReplacementsExtractor(LocaleExtractor localeExtractor, ReplacementsExtractor replacementsExtractor) {
        this.localeExtractor = localeExtractor;
        this.replacementsExtractor = replacementsExtractor;
    }

    public Result extractor (Environment environment, Object input) {
        Optional<Locale> localeExtract = localeExtractor.extract(environment, input);
        if (localeExtract.isPresent()) {
            return new Result(localeExtract, Optional.<Collection<ReplacementMessageDecorator.Replacement>>absent());
        } else {
            Optional<Collection<ReplacementMessageDecorator.Replacement>> collectionOptional = replacementsExtractor.extract(environment, input);
            if (collectionOptional.isPresent()) {
                return new Result(Optional.<Locale>absent(), collectionOptional);
            } else {
                return new Result(Optional.<Locale>absent(), Optional.<Collection<ReplacementMessageDecorator.Replacement>>absent());
            }
        }
    }

    public static class Result {
        private final Optional<Locale> locale;
        private final Optional<Collection<ReplacementMessageDecorator.Replacement>> replacements;

        public Result(Optional<Locale> locale, Optional<Collection<ReplacementMessageDecorator.Replacement>> replacements) {
            this.locale = locale;
            this.replacements = replacements;
        }

        public Optional<Locale> getLocale() {
            return locale;
        }

        public Optional<Collection<ReplacementMessageDecorator.Replacement>> getReplacements() {
            return replacements;
        }

        public boolean isEmpty () {
            return !locale.isPresent() && !replacements.isPresent();
        }
    }
}
