package org.alahijani.lf.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.TwelfElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfElementImpl extends StubBasedPsiElementBase<StubElement> implements TwelfElement {

    public TwelfElementImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public TwelfElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    // can be useful in implementing resolve()
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        return super.processDeclarations(processor, state, lastParent, place);
    }
}
