package org.alahijani.lf.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.alahijani.lf.psi.TwelfStubElementType;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.impl.LfDeclarationImpl;
import org.alahijani.lf.psi.stubs.LfDeclarationStub;
import org.alahijani.lf.psi.stubs.impl.LfDeclarationStubImpl;

import java.io.IOException;

/**
 * @author Ali Lahijani
 */
public class LfDeclarationElementType extends TwelfStubElementType<LfDeclarationStub, LfDeclaration> {
  public LfDeclarationElementType(String debugName) {
    super(debugName);
  }

    @Override
    public PsiElement createElement(ASTNode node) {
        return new LfDeclarationImpl(node);
    }

    @Override
    public LfDeclaration createPsi(LfDeclarationStub stub) {
        return new LfDeclarationImpl(stub, this);
    }

    @Override
    public LfDeclarationStub createStub(LfDeclaration psi, StubElement parentStub) {
        return new LfDeclarationStubImpl(parentStub, this, psi.getName());
    }

    public void serialize(LfDeclarationStub stub, StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
    }

    public LfDeclarationStub deserialize(StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new LfDeclarationStubImpl(parentStub, this, dataStream.readName().getString());
    }

}
