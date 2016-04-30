package org.jtwig.translate.message.source.factory;

import org.jtwig.environment.Environment;
import org.jtwig.translate.message.source.MessageSource;

public interface MessageSourceFactory {
    MessageSource create (Environment environment);
}
