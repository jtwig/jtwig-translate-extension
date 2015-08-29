package org.jtwig.translate.parser;

import org.jtwig.parser.addon.AddonParserProvider;
import org.jtwig.parser.parboiled.node.AddonParser;

import java.util.Collection;

public class TranslateAddonParserProvider implements AddonParserProvider {
    @Override
    public Class<? extends AddonParser> parser() {
        return TranslateNodeParser.class;
    }

    @Override
    public Collection<String> keywords() {
        return TranslateKeywords.all();
    }
}
