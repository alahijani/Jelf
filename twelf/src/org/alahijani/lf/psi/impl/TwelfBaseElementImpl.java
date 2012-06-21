package org.alahijani.lf.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.TwelfBaseElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfBaseElementImpl extends StubBasedPsiElementBase<StubElement> implements TwelfBaseElement {

    public TwelfBaseElementImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public TwelfBaseElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    // can be useful in implementing resolve()
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        return super.processDeclarations(processor, state, lastParent, place);
    }

    @NotNull
    @Override
    public GlobalSearchScope getResolveScope() {
        // current versions of Twelf require all cross-referencing elements to reside in files in the same directory
        PsiDirectory directory = getContainingFile().getContainingDirectory();
        return directory == null
                ? GlobalSearchScope.allScope(getProject())
                : GlobalSearchScope.directoryScope(directory, false);
    }

    @NotNull
    @Override
    public SearchScope getUseScope() {
        return super.getUseScope();
    }

}
