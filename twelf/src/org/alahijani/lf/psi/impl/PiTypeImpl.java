package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.LfTerm;
import org.alahijani.lf.psi.api.PiType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class PiTypeImpl extends LocalVariableBinderImpl implements PiType {
    public PiTypeImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public PiTypeImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfTerm getParameterType() {
        return getBoundDeclaration().getType();
    }

    public LfTerm getReturnType() {
        return findChildByClass(LfTerm.class);
    }
}
