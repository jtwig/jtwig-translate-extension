package org.jtwig.translate.message.source.cache;

import org.jtwig.environment.Environment;
import org.jtwig.translate.message.source.MessageSource;
import org.jtwig.translate.message.source.factory.MessageSourceFactory;

public class CachedMessageSourceFactory implements MessageSourceFactory {
    public static CachedMessageSourceFactory cachedWith (MessageSourceCache cache, MessageSourceFactory factory) {
        return new CachedMessageSourceFactory(cache, factory);
    }

    private final MessageSourceCache messageSourceCache;
    private final MessageSourceFactory delegate;

    public CachedMessageSourceFactory(MessageSourceCache messageSourceCache, MessageSourceFactory delegate) {
        this.messageSourceCache = messageSourceCache;
        this.delegate = delegate;
    }

    @Override
    public MessageSource create(Environment environment) {
        return new CachedMessageSource(
                messageSourceCache,
                delegate.create(environment)
        );
    }
}
