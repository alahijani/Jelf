package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;

/**
* @author Ali Lahijani
*/
public class TwelfFileReferenceImpl extends LfIdentifierReferenceImpl {
    public TwelfFileReferenceImpl(ASTNode node) {
        super(node);
    }

    @Override
    protected PsiElement doResolve() {
        PsiDirectory directory = getContainingFile().getParent();
        return directory == null ? null : directory.findFile(getIdentifier().getText());
    }
}
