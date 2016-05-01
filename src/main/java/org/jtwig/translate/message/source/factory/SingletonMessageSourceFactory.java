package org.jtwig.translate.message.source.factory;

import org.jtwig.environment.Environment;
import org.jtwig.translate.message.source.MessageSource;

public class SingletonMessageSourceFactory implements MessageSourceFactory {
    private final MessageSource singleton;

    public SingletonMessageSourceFactory(MessageSource singleton) {
        this.singleton = singleton;
    }

    @Override
    public MessageSource create(Environment environment) {
        return singleton;
    }
}
