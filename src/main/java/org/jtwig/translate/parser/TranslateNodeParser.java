package org.jtwig.translate.parser;

import org.jtwig.parser.parboiled.ParserContext;
import org.jtwig.parser.parboiled.base.LexicParser;
import org.jtwig.parser.parboiled.base.LimitsParser;
import org.jtwig.parser.parboiled.base.PositionTrackerParser;
import org.jtwig.parser.parboiled.base.SpacingParser;
import org.jtwig.parser.parboiled.expression.AnyExpressionParser;
import org.jtwig.parser.parboiled.node.AddonParser;
import org.jtwig.parser.parboiled.node.CompositeNodeParser;
import org.jtwig.translate.node.TranslateNode;
import org.parboiled.Rule;

public class TranslateNodeParser extends AddonParser {
    public TranslateNodeParser(ParserContext context) {
        super(TranslateNodeParser.class, context);
    }

    @Override
    public Rule NodeRule() {
        PositionTrackerParser positionTrackerParser = parserContext().parser(PositionTrackerParser.class);
        LimitsParser limitsParser = parserContext().parser(LimitsParser.class);
        SpacingParser spacingParser = parserContext().parser(SpacingParser.class);
        LexicParser lexicParser = parserContext().parser(LexicParser.class);
        AnyExpressionParser anyExpressionParser = parserContext().parser(AnyExpressionParser.class);
        CompositeNodeParser compositeNodeParser = parserContext().parser(CompositeNodeParser.class);
        return Sequence(
                positionTrackerParser.PushPosition(),
                Sequence(
                        limitsParser.startCode(),
                        spacingParser.Spacing(),
                        lexicParser.Keyword(TranslateKeywords.TRANSLATE.toString()),
                        FirstOf(
                                Sequence(
                                        spacingParser.Spacing(),
                                        lexicParser.Keyword(TranslateKeywords.WITH.toString()),
                                        spacingParser.Spacing(),
                                        anyExpressionParser.ExpressionRule()
                                ),
                                anyExpressionParser.push(null)
                        ),
                        FirstOf(
                                Sequence(
                                        spacingParser.Spacing(),
                                        lexicParser.Keyword(TranslateKeywords.INTO.toString()),
                                        spacingParser.Spacing(),
                                        anyExpressionParser.ExpressionRule()
                                ),
                                anyExpressionParser.push(null)
                        ),

                        spacingParser.Spacing(),
                        limitsParser.endCode()
                ),

                compositeNodeParser.NodeRule(),

                Sequence(
                        limitsParser.startCode(),
                        spacingParser.Spacing(),
                        lexicParser.Keyword(TranslateKeywords.END_TRANSLATE.toString()),
                        spacingParser.Spacing(),
                        limitsParser.endCode()
                ),

                push(new TranslateNode(positionTrackerParser.pop(3), compositeNodeParser.pop(), anyExpressionParser.pop(1), anyExpressionParser.pop()))
        );
    }
}
