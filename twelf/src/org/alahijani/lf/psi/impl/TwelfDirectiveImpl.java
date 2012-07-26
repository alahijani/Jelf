package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.TwelfDirective;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfDirectiveImpl extends TwelfStatementImpl implements TwelfDirective {

    public TwelfDirectiveImpl(@NotNull ASTNode node) {
        super(node);
    }

    public String getDirectiveName() {
        PsiElement directiveName = findChildByType(TwelfTokenType.DIRECTIVE);
        return directiveName == null ? null : directiveName.getText();
    }

}
