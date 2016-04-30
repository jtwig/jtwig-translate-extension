package org.jtwig.translate.message.decorate;

import java.util.Collection;

public class ReplacementMessageDecorator implements MessageDecorator {
    private final Collection<Replacement> replacements;

    public ReplacementMessageDecorator(Collection<Replacement> replacements) {
        this.replacements = replacements;
    }

    @Override
    public String decorate(String message) {
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getFind(), replacement.getReplacement());
        }
        return message;
    }

    public static class Replacement {
        private final String find;
        private final String replace;

        public Replacement(String find, String replace) {
            this.find = find;
            this.replace = replace;
        }

        public String getFind() {
            return find;
        }

        public String getReplacement() {
            return replace;
        }
    }
}
