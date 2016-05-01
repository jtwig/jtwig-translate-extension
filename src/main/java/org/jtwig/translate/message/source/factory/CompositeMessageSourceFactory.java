package org.jtwig.translate.message.source.factory;

import org.jtwig.environment.Environment;
import org.jtwig.translate.message.source.CompositeMessageSource;
import org.jtwig.translate.message.source.MessageSource;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;

public class CompositeMessageSourceFactory implements MessageSourceFactory {
    public static CompositeMessageSourceFactory multiple (MessageSourceFactory... factories) {
        return new CompositeMessageSourceFactory(asList(factories));
    }

    private final Collection<MessageSourceFactory> messageSourceFactories;

    public CompositeMessageSourceFactory(Collection<MessageSourceFactory> messageSourceFactories) {
        this.messageSourceFactories = messageSourceFactories;
    }

    @Override
    public MessageSource create(Environment environment) {
        Collection<MessageSource> messageSources = new ArrayList<>();
        for (MessageSourceFactory messageSourceFactory : messageSourceFactories) {
            messageSources.add(messageSourceFactory.create(environment));
        }
        return new CompositeMessageSource(messageSources);
    }
}
