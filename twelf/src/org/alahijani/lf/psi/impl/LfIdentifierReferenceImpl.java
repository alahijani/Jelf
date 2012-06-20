package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.LfIdentifier;
import org.alahijani.lf.psi.api.LfIdentifierReference;
import org.alahijani.lf.psi.xref.Referencing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ali Lahijani
 */
public class LfIdentifierReferenceImpl extends TwelfElementImpl implements LfIdentifierReference {

    public LfIdentifierReferenceImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiReference getReference() {
        return this;
    }

    public PsiElement getElement() {
        return this;
    }

    @Nullable
    public PsiElement getReferenceNameElement() {
        return getIdentifier();
    }

    public LfIdentifier getIdentifier() {
        return findChildByClass(LfIdentifier.class);
    }

    public TextRange getRangeInElement() {
        final PsiElement refNameElement = getReferenceNameElement();
        if (refNameElement != null) {
            final int offsetInParent = refNameElement.getStartOffsetInParent();
            return new TextRange(offsetInParent, offsetInParent + refNameElement.getTextLength());
        }
        return new TextRange(0, getTextLength());
    }

    @NotNull
    public String getCanonicalText() {
        return getRangeInElement().substring(getElement().getText());
    }

    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        if (isReferenceTo(element)) return this;

        if (element instanceof LfDeclaration) {
            handleElementRename(((LfDeclaration) element).getName());
        }

        throw new IncorrectOperationException("Cannot bind to:" + element + " of class " + element.getClass());
    }

    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        Referencing.rename(getIdentifier(), newElementName);
        return this;
    }

    /*
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        ElementManipulator<LfIdentifierReferenceImpl> manipulator = ElementManipulators.getManipulator(this);
        assert manipulator != null: "Cannot find manipulator for " + this;
        return manipulator.handleContentChange(this, getRangeInElement(), newElementName);
    }
*/
/*
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        LfIdentifier myIdentifier = getIdentifier();
        if (myIdentifier != null) {
            LfIdentifier identifier = TwelfPsiElementFactory.getInstance(getProject()).createIdentifier(newElementName);
            myIdentifier.replace(identifier);
        }

        return this;
    }
*/

    public boolean isReferenceTo(PsiElement element) {
        if (element instanceof PsiNamedElement) {
            return getManager().areElementsEquivalent(element, correctSearchTargets(resolve()));
        }
        return false;
    }

    @Nullable
    public static PsiElement correctSearchTargets(@Nullable PsiElement target) {
        if (target != null && !target.isPhysical()) {
            return target.getNavigationElement();
        }
        return target;
    }

    @NotNull
    public Object[] getVariants() {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }


    public boolean isSoft() {
        return false;
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TwelfElementVisitor) {
            ((TwelfElementVisitor) visitor).visitReference(this);
        } else {
            visitor.visitElement(this);
        }
    }

    public PsiElement resolve() {
        return getManager().getResolveCache().resolveWithCaching(this, RESOLVER, true, false);
    }

/*
    @NotNull
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        return getManager().getResolveCache().resolveWithCaching(this, POLY_VARIANT_RESOLVER, true, false);
    }
*/

    private static final ResolveCache.Resolver RESOLVER = new ResolveCache.Resolver() {
        public PsiElement resolve(PsiReference psiReference, boolean incompleteCode) {
            return ((LfIdentifierReferenceImpl) psiReference).doResolve();
        }
    };

/*
    private static final ResolveCache.PolyVariantResolver<LfIdentifierReferenceImpl> POLY_VARIANT_RESOLVER = new ResolveCache.PolyVariantResolver<LfIdentifierReferenceImpl>() {
        public ResolveResult[] resolve(LfIdentifierReferenceImpl lfIdentifierReference, boolean incompleteCode) {
            return lfIdentifierReference.doMultiResolve();
        }
    };
*/

    protected PsiElement doResolve() {
        return Referencing.resolveTarget(this);
    }

/*
    @NotNull
    public ResolveResult[] doMultiResolve() {
        return Referencing.multiResolveTarget(this);
    }
*/

}
