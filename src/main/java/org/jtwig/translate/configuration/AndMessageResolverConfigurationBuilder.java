package org.jtwig.translate.configuration;

import org.jtwig.environment.and.AndBuilder;
import org.jtwig.i18n.MessageResolverConfiguration;
import org.jtwig.i18n.MessageResolverConfigurationBuilder;

public class AndMessageResolverConfigurationBuilder extends MessageResolverConfigurationBuilder<AndMessageResolverConfigurationBuilder> implements AndBuilder<TranslateConfigurationBuilder> {
    private final TranslateConfigurationBuilder parent;

    public AndMessageResolverConfigurationBuilder(TranslateConfigurationBuilder parent) {
        this.parent = parent;
    }

    public AndMessageResolverConfigurationBuilder(MessageResolverConfiguration prototype, TranslateConfigurationBuilder parent) {
        super(prototype);
        this.parent = parent;
    }

    @Override
    public TranslateConfigurationBuilder and() {
        return parent;
    }
}
