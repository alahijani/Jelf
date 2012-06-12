package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.ParenthesizedExpression;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class ParenthesizedExpressionImpl extends TwelfElementImpl implements ParenthesizedExpression {
    public ParenthesizedExpressionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public ParenthesizedExpressionImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }
}
