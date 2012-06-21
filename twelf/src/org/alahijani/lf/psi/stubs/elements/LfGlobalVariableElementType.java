package org.alahijani.lf.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.alahijani.lf.psi.TwelfStubElementType;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.alahijani.lf.psi.impl.LfGlobalVariableImpl;
import org.alahijani.lf.psi.stubs.LfGlobalVariableStub;
import org.alahijani.lf.psi.stubs.impl.LfGlobalVariableStubImpl;
import org.alahijani.lf.psi.stubs.index.LfGlobalVariableIndex;

import java.io.IOException;

/**
 * @author Ali Lahijani
 */
public class LfGlobalVariableElementType extends TwelfStubElementType<LfGlobalVariableStub, LfGlobalVariable> {
    public LfGlobalVariableElementType(String debugName) {
        super(debugName);
    }

    @Override
    public PsiElement createElement(ASTNode node) {
        return new LfGlobalVariableImpl(node);
    }

    @Override
    public LfGlobalVariable createPsi(LfGlobalVariableStub stub) {
        return new LfGlobalVariableImpl(stub, this);
    }

    @Override
    public LfGlobalVariableStub createStub(LfGlobalVariable psi, StubElement parentStub) {
        return new LfGlobalVariableStubImpl(parentStub, this, psi.getName());
    }

    public void serialize(LfGlobalVariableStub stub, StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
    }

    public LfGlobalVariableStub deserialize(StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new LfGlobalVariableStubImpl(parentStub, this, dataStream.readName().getString());
    }

    @Override
    public void indexStub(LfGlobalVariableStub stub, IndexSink sink) {
        sink.occurrence(LfGlobalVariableIndex.KEY, stub.getName());
    }
}
