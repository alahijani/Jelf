package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.LocalVariableBinder;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LocalVariableBinderImpl extends TwelfElementImpl implements LocalVariableBinder {
    public LocalVariableBinderImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public LocalVariableBinderImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfDeclaration getBoundDeclaration() {
        return findChildByClass(LfDeclaration.class);
    }
}
