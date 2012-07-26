package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.LambdaExpression;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LambdaExpressionImpl extends LocalVariableBinderImpl implements LambdaExpression {

    public LambdaExpressionImpl(@NotNull ASTNode node) {
        super(node);
    }
}
