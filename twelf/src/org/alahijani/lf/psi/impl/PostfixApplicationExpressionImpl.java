package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.ApplicationExpression;
import org.alahijani.lf.psi.api.LfTerm;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class PostfixApplicationExpressionImpl extends TwelfElementImpl implements ApplicationExpression {

    public PostfixApplicationExpressionImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public PostfixApplicationExpressionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfTerm getFunction() {
        LfTerm[] terms = findChildrenByClass(LfTerm.class);
        return terms.length > 1 ? terms[1] : null;
    }

    public LfTerm getArgument() {
        LfTerm[] terms = findChildrenByClass(LfTerm.class);
        return terms.length > 0 ? terms[0] : null;
    }
}
