package org.jtwig.translate.message.source.localized.resource.locale;

import com.google.common.base.Optional;

public class RetrieveLocaleExpressionFromFilename {
    private static final RetrieveLocaleExpressionFromFilename INSTANCE = new RetrieveLocaleExpressionFromFilename();

    public static RetrieveLocaleExpressionFromFilename instance () {
        return INSTANCE;
    }

    private RetrieveLocaleExpressionFromFilename() {}

    public Optional<String> retrieveLocaleExpression(String name) {
        int lastIndexOf = name.lastIndexOf(".");

        if (lastIndexOf > 0) {
            String result = name.substring(0, lastIndexOf);
            int position = result.lastIndexOf(".");
            if (position > 0 && position < result.length() - 1) {
                return Optional.of(result.substring(position + 1));
            }
        }

        return Optional.absent();
    }
}
