package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.PiType;
import org.alahijani.lf.psi.light.LightLfDeclaration;

/**
 * @author Ali Lahijani
 */
public abstract class ArrowTypeImpl extends TwelfElementImpl implements PiType {
    private LfDeclaration declaration;

    public ArrowTypeImpl(ASTNode node) {
        super(node);
    }

    public ArrowTypeImpl(StubElement stub, IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public LfDeclaration getBoundDeclaration() {
        if (declaration == null) {
            declaration = LightLfDeclaration.declareAnonymous(this, getParameterType());
        }
        return declaration;
    }
}
