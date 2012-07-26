package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.FromArrowType;
import org.alahijani.lf.psi.api.LfTerm;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class FromArrowTypeImpl extends ArrowTypeImpl implements FromArrowType {

    public FromArrowTypeImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfTerm getParameterType() {
        LfTerm[] children = findChildrenByClass(LfTerm.class);
        return children.length > 1 ? children[1] : null;
    }

    public LfTerm getReturnType() {
        LfTerm[] children = findChildrenByClass(LfTerm.class);
        return children.length > 0 ? children[0] : null;
    }
}

