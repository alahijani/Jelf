package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.StringExpression;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class StringExpressionImpl extends TwelfElementImpl implements StringExpression {

    public StringExpressionImpl(@NotNull ASTNode node) {
        super(node);
    }
}
