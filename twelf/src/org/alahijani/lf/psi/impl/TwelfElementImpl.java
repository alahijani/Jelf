package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.TwelfElement;
import org.alahijani.lf.psi.api.TwelfFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfElementImpl extends TwelfBaseElementImpl implements TwelfElement {
    public TwelfElementImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public TwelfElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public TwelfFile getContainingFile() {
        return (TwelfFile) super.getContainingFile();
    }
}
