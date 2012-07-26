package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.LfLocalVariable;
import org.alahijani.lf.psi.api.LocalVariableBinder;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LocalVariableBinderImpl extends TwelfElementImpl implements LocalVariableBinder {

    public LocalVariableBinderImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfLocalVariable getBoundDeclaration() {
        return findChildByClass(LfLocalVariable.class);
    }
}
