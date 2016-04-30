package org.jtwig.translate.message.decorate;

import java.util.Collection;

public class CompositeMessageDecorator implements MessageDecorator {
    private final Collection<MessageDecorator> messageDecorators;

    public CompositeMessageDecorator(Collection<MessageDecorator> messageDecorators) {
        this.messageDecorators = messageDecorators;
    }

    @Override
    public String decorate(String message) {
        for (MessageDecorator messageDecorator : messageDecorators) {
            message = messageDecorator.decorate(message);
        }
        return message;
    }
}
