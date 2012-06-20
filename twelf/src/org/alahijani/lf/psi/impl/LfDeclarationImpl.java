package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.stubs.LfDeclarationStub;
import org.alahijani.lf.psi.xref.Referencing;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LfDeclarationImpl extends TwelfBaseElementImpl<LfDeclarationStub> implements LfDeclaration, StubBasedPsiElement<LfDeclarationStub> {

    public LfDeclarationImpl(final LfDeclarationStub stub, IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public LfDeclarationImpl(final ASTNode node) {
        super(node);
    }

    public LfTerm getValue() {
        return findChildByClass(LfTerm.class);
    }

    public boolean isFileLevel() {
        return (getParent() instanceof GlobalVariableBinder);
    }

    public boolean isMeta() {
        return false;
    }

    public boolean isAnonymous() {
        return getNameIdentifier().isAnonymous();
    }

    public LfIdentifier getNameIdentifier() {
        return findChildByClass(LfIdentifier.class);
    }

    public LfTypeElement getTypeElement() {
        return findChildByClass(LfTypeElement.class);
    }

    public LfTerm getType() {
        ASTNode colon = getNode().findChildByType(TwelfTokenType.COLON);
        if (colon == null) {
            return null;
        }

        // Skip whitespaces, etc.
        for (ASTNode it = colon.getTreeNext(); it != null; it = it.getTreeNext()) {

            PsiElement psi = it.getPsi();
            if (psi instanceof LfTerm) {
                return (LfTerm) psi;
            }
        }

        return null;
    }

    @NotNull
    @Override
    public String getName() {
        return getNameIdentifier().getText();
    }

/*
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        PsiElement nameElement = getNameIdentifier();
        TwelfPsiElementFactory factory = TwelfPsiElementFactory.getInstance(nameElement.getProject());
        LfIdentifier newNameIdentifier = factory.createIdentifier(name);
        nameElement.replace(newNameIdentifier);

        return this;
    }
*/
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        Referencing.rename(getNameIdentifier(), name);
        return this;
    }

    @Override
    public String toString() {
        return "variable '" + getText() + "'";
    }
}
