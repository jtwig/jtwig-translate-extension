package org.jtwig.translate.message.source;

import org.jtwig.environment.Environment;

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
