package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import org.alahijani.lf.psi.api.TwelfFile;
import org.alahijani.lf.psi.api.TwelfFileReference;
import org.jetbrains.annotations.NotNull;

/**
* @author Ali Lahijani
*/
public class TwelfFileReferenceImpl extends TwelfIdentifierReferenceImpl<TwelfFile> implements TwelfFileReference {
    public TwelfFileReferenceImpl(ASTNode node) {
        super(node);
    }

    public TwelfFile resolve() {
        return (TwelfFile) ResolveCache.getInstance(getProject()).resolveWithCaching(this, RESOLVER, true, false);
    }

    private static final ResolveCache.Resolver RESOLVER = new ResolveCache.Resolver() {
        public PsiElement resolve(PsiReference psiReference, boolean incompleteCode) {
            return ((TwelfFileReferenceImpl) psiReference).doResolve();
        }
    };

    protected PsiElement doResolve() {
        PsiDirectory directory = getContainingFile().getParent();
        return directory == null ? null : directory.findFile(getIdentifier().getText());
    }

    @NotNull
    protected ResolveResult[] doMultiResolve() {
        PsiElement element = doResolve();
        return element == null
                ? ResolveResult.EMPTY_ARRAY
                : new ResolveResult[]{new PsiElementResolveResult(element)};
    }

}
