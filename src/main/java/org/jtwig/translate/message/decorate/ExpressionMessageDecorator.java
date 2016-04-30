package org.jtwig.translate.message.decorate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionMessageDecorator implements MessageDecorator {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("%(\\w+)%");

    private final ReplacementFinder replacementFinder;

    public ExpressionMessageDecorator(ReplacementFinder replacementFinder) {
        this.replacementFinder = replacementFinder;
    }

    @Override
    public String decorate(String message) {
        StringBuilder result = new StringBuilder();
        Matcher matcher = VARIABLE_PATTERN.matcher(message);
        int lastVariableEnd = 0;
        while (matcher.find()) {
            result.append(message.substring(0, matcher.start()));
            result.append(replacementFinder.replacementFor(matcher.group(1)));
            lastVariableEnd = matcher.end();
        }

        result.append(message.substring(lastVariableEnd));

        return result.toString();
    }

    public interface ReplacementFinder {
        String replacementFor(String value);
    }
}
