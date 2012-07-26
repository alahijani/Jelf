package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiBundle;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.formatter.TwelfFormatter;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.stubs.LfGlobalVariableStub;
import org.alahijani.lf.psi.util.TwelfPsiUtil;
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
        ASTNode eq = getNode().findChildByType(TwelfTokenType.KEYWORD);
        if (eq == null) return null;

        assert "=".equals(eq.getText());

        return PsiTreeUtil.getNextSiblingOfType(eq.getPsi(), LfTerm.class);
    }

    public TwelfIdentifier getNameIdentifier() {
        return findChildByClass(TwelfIdentifier.class);
    }

    public LfTypeElement getTypeElement() {
        return findChildByClass(LfTypeElement.class);
    }

    public LfTerm getType() {
        ASTNode colon = getNode().findChildByType(TwelfTokenType.COLON);
        if (colon == null) return null;

        return PsiTreeUtil.getNextSiblingOfType(colon.getPsi(), LfTerm.class);
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

    @NotNull
    @Override
    public SearchScope getUseScope() {
        PsiDirectory directory = getContainingFile().getContainingDirectory();
        return directory == null
                ? GlobalSearchScope.allScope(getProject())
                : GlobalSearchScope.directoryScope(directory, false);
    }

    public TwelfBaseElement getVirtualParent() {
        return (TwelfBaseElement) getParent();
    }

    public Iterable<? extends TwelfBaseElement> getVirtualParentChain() {
        return TwelfPsiUtil.getVirtualParentChain(this);
    }
}
