package org.jtwig.translate.message.source;

import org.jtwig.environment.Environment;

public interface MessageSourceFactory {
    MessageSource create (Environment environment);
}
