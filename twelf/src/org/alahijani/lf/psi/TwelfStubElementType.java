package org.alahijani.lf.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.Twelf;
import org.alahijani.lf.psi.api.TwelfElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfStubElementType<S extends StubElement, T extends TwelfElement> extends IStubElementType<S, T> {

  public TwelfStubElementType(@NonNls @NotNull String debugName) {
    super(debugName, Twelf.INSTANCE);
  }

  public abstract PsiElement createElement(final ASTNode node);

  public void indexStub(final S stub, final IndexSink sink) {
  }

  public String getExternalId() {
    return "twelf." + super.toString();
  }

}
