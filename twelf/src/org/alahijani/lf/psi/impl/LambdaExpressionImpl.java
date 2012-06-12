package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.LambdaExpression;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LambdaExpressionImpl extends LocalVariableBinderImpl implements LambdaExpression {
    public LambdaExpressionImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public LambdaExpressionImpl(@NotNull ASTNode node) {
        super(node);
    }
}
