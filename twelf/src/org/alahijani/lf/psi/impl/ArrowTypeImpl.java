package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.LfLocalVariable;
import org.alahijani.lf.psi.api.PiType;
import org.alahijani.lf.psi.light.LightLfDeclaration;

/**
 * @author Ali Lahijani
 */
public abstract class ArrowTypeImpl extends TwelfElementImpl implements PiType {
    private LfLocalVariable declaration;

    public ArrowTypeImpl(ASTNode node) {
        super(node);
    }

    public LfLocalVariable getBoundDeclaration() {
        if (declaration == null) {
            declaration = LightLfDeclaration.createAnonymousLocal(this, getParameterType());
        }
        return declaration;
    }
}
