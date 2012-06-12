package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.LfTerm;
import org.alahijani.lf.psi.api.ToArrowType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class ToArrowTypeImpl extends ArrowTypeImpl implements ToArrowType {

    public ToArrowTypeImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public ToArrowTypeImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfTerm getParameterType() {
        return findChildrenByClass(LfTerm.class)[0];
    }

    public LfTerm getReturnType() {
        return findChildrenByClass(LfTerm.class)[1];
    }
}
