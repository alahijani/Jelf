package org.alahijani.lf.psi.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.stubs.LfDeclarationStub;

/**
* @author Ali Lahijani
*/
public class LfDeclarationStubImpl extends StubBase<LfDeclaration> implements LfDeclarationStub {
    private String name;
    private Throwable throwable = new Throwable();

    public LfDeclarationStubImpl(StubElement parent, IStubElementType elementType, String name) {
        super(parent, elementType);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
