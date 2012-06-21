package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiBundle;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.formatter.TwelfFormatter;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.alahijani.lf.psi.api.LfTerm;
import org.alahijani.lf.psi.api.LfTypeElement;
import org.alahijani.lf.psi.api.TwelfIdentifier;
import org.alahijani.lf.psi.stubs.LfGlobalVariableStub;
import org.alahijani.lf.psi.xref.Referencing;
import org.alahijani.lf.structure.TwelfItemPresentation;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Ali Lahijani
 */
public class LfGlobalVariableImpl extends TwelfStubBasedElementImpl<LfGlobalVariableStub>
        implements LfGlobalVariable {

    public LfGlobalVariableImpl(final LfGlobalVariableStub stub, IStubElementType<LfGlobalVariableStub, LfGlobalVariable> nodeType) {
        super(stub, nodeType);
    }

    public LfGlobalVariableImpl(final ASTNode node) {
        super(node);
    }

    public LfTerm getValue() {
        return findChildByClass(LfTerm.class);
    }

    public TwelfIdentifier getNameIdentifier() {
        return findChildByClass(TwelfIdentifier.class);
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
    public String getName() {
        return getNameIdentifier().getText();
    }

    public String getTypeText() {
        return TwelfFormatter.format(getType());
    }

    /*
        public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
            PsiElement nameElement = getNameIdentifier();
            TwelfPsiElementFactory factory = TwelfPsiElementFactory.getInstance(nameElement.getProject());
            TwelfIdentifier newNameIdentifier = factory.createIdentifier(name);
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
        return "'" + getText() + "' in " + getContainingFile().getName();
    }

    @Override
    public ItemPresentation getPresentation() {
        return new TwelfItemPresentation<LfGlobalVariableImpl>(this) {
            public String getPresentableText() {
                return getName();
            }

            @Override
            public String getLocationString() {
                return PsiBundle.message("aux.context.display", getContainingFile().getName());
            }

            @Override
            public TextAttributesKey getTextAttributesKey() {
                if (getValue() != null) {
                    return null;    // todo
                }
                return null;
            }
        };
    }

    @Override
    public Icon getIcon(int flags) {
        return TwelfIcons.LF_GLOBAL_VARIABLE;
    }

}
