package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import org.alahijani.lf.psi.api.LfLocalVariable;
import org.alahijani.lf.psi.stubs.LfGlobalVariableStub;

/**
 * @author Ali Lahijani
 */
public class LfLocalVariableImpl extends LfDeclarationImpl implements LfLocalVariable {
    public LfLocalVariableImpl(final LfGlobalVariableStub stub, IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public LfLocalVariableImpl(final ASTNode node) {
        super(node);
    }
}
