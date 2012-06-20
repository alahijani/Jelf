package org.alahijani.lf.psi.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.alahijani.lf.psi.stubs.LfGlobalVariableStub;

/**
* @author Ali Lahijani
*/
public class LfGlobalVariableStubImpl extends StubBase<LfGlobalVariable> implements LfGlobalVariableStub {
    private String name;
    // private Throwable throwable = new Throwable();

    public LfGlobalVariableStubImpl(StubElement parent, IStubElementType elementType, String name) {
        super(parent, elementType);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
