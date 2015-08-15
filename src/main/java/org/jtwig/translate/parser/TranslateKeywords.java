package org.jtwig.translate.parser;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.Collection;

import static java.util.Arrays.asList;

public enum TranslateKeywords {
    TRANSLATE("trans"),
    END_TRANSLATE("endtrans"),
    WITH("with"),
    INTO("into")
    ;
    public static Collection<String> all () {
        return Collections2.transform(asList(values()), new Function<TranslateKeywords, String>() {
            @Override
            public String apply(TranslateKeywords input) {
                return input.token;
            }
        });
    }

    private final String token;

    TranslateKeywords(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token;
    }
}
