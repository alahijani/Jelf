package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.TypeKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TypeKeywordImpl extends TwelfElementImpl implements TypeKeyword {

    public TypeKeywordImpl(@NotNull ASTNode node) {
        super(node);
    }
}
