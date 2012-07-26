package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.LfTerm;
import org.alahijani.lf.psi.api.ParenthesizedExpression;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class ParenthesizedExpressionImpl extends TwelfElementImpl implements ParenthesizedExpression {
    public ParenthesizedExpressionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfTerm getWrapped() {
        return findChildByClass(LfTerm.class);
    }
}
