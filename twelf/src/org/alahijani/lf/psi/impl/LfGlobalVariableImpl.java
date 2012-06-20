package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.alahijani.lf.psi.api.LfTerm;
import org.alahijani.lf.psi.stubs.LfGlobalVariableStub;

/**
 * @author Ali Lahijani
 */
public class LfGlobalVariableImpl extends LfDeclarationImpl implements LfGlobalVariable {
    public LfGlobalVariableImpl(final LfGlobalVariableStub stub, IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public LfGlobalVariableImpl(final ASTNode node) {
        super(node);
    }

    public LfTerm getValue() {
        return findChildByClass(LfTerm.class);
    }
}
