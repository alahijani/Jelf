package org.alahijani.lf.psi.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.alahijani.lf.psi.stubs.LfGlobalVariableStub;
import org.jetbrains.annotations.NotNull;

/**
* @author Ali Lahijani
*/
public class LfGlobalVariableStubImpl extends StubBase<LfGlobalVariable> implements LfGlobalVariableStub {
    @NotNull
    private String name;
    // private Throwable throwable = new Throwable();

    public LfGlobalVariableStubImpl(StubElement parent, IStubElementType elementType, @NotNull String name) {
        super(parent, elementType);
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
