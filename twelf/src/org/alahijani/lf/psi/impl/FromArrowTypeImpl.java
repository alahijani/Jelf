package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.FromArrowType;
import org.alahijani.lf.psi.api.LfTerm;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class FromArrowTypeImpl extends ArrowTypeImpl implements FromArrowType {
    public FromArrowTypeImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public FromArrowTypeImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfTerm getParameterType() {
        return findChildrenByClass(LfTerm.class)[1];
    }

    public LfTerm getReturnType() {
        return findChildrenByClass(LfTerm.class)[0];
    }
}

