package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.LfTerm;
import org.alahijani.lf.psi.api.TypedLfTerm;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TypedLfTermImpl extends TwelfElementImpl implements TypedLfTerm {

    public TypedLfTermImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfTerm getWrapped() {
        return findChildByClass(LfTerm.class);      // returns the first one
    }

    public LfTerm getType() {
        ASTNode colon = getNode().findChildByType(TwelfTokenType.COLON);
        if (colon == null) return null;

        return PsiTreeUtil.getNextSiblingOfType(colon.getPsi(), LfTerm.class);
    }
}
