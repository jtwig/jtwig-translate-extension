package org.jtwig.translate.node;

import com.google.common.base.Optional;
import org.jtwig.model.expression.Expression;
import org.jtwig.model.position.Position;
import org.jtwig.model.tree.ContentNode;
import org.jtwig.model.tree.Node;

public class TranslateNode extends ContentNode {
    private final Optional<Expression> withExpression;
    private final Optional<Expression> localeExpression;

    public TranslateNode(Position position, Node content, Expression withExpression, Expression localeExpression) {
        super(position, content);
        this.withExpression = Optional.fromNullable(withExpression);
        this.localeExpression = Optional.fromNullable(localeExpression);
    }

    public Optional<Expression> getWithExpression() {
        return withExpression;
    }

    public Optional<Expression> getLocaleExpression() {
        return localeExpression;
    }
}
