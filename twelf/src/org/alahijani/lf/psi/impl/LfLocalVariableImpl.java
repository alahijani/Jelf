package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.editor.TwelfHighlighterColors;
import org.alahijani.lf.formatter.TwelfFormatter;
import org.alahijani.lf.psi.api.TwelfIdentifier;
import org.alahijani.lf.psi.api.LfLocalVariable;
import org.alahijani.lf.psi.api.LfTerm;
import org.alahijani.lf.psi.api.LfTypeElement;
import org.alahijani.lf.psi.xref.Referencing;
import org.alahijani.lf.structure.TwelfItemPresentation;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Ali Lahijani
 */
public class LfLocalVariableImpl extends TwelfElementImpl implements LfLocalVariable {

    public LfLocalVariableImpl(final ASTNode node) {
        super(node);
    }

    public TwelfIdentifier getNameIdentifier() {
        return findChildByClass(TwelfIdentifier.class);
    }

    public LfTerm getType() {
        return findChildByClass(LfTerm.class);
    }

    public LfTypeElement getTypeElement() {
        return findChildByClass(LfTypeElement.class);
    }

    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        Referencing.rename(getNameIdentifier(), name);
        return this;
    }

    @NotNull
    public String getName() {
        return getNameIdentifier().getText();
    }

    public String getTypeText() {
        return TwelfFormatter.format(getType());
    }

    @Override
    public String toString() {
        return "Local '" + getText() + "'";
    }

    @Override
    public Icon getIcon(int flags) {
        return TwelfIcons.LF_LOCAL_VARIABLE;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new TwelfItemPresentation<LfLocalVariableImpl>(this) {
            public String getPresentableText() {
                return getName();
            }

            @Override
            public TextAttributesKey getTextAttributesKey() {
                return TwelfHighlighterColors.LF_LOCAL_IDENTIFIER;
            }
        };
    }

    @NotNull
    @Override
    public SearchScope getUseScope() {
        return new LocalSearchScope(getParent());
    }
}
