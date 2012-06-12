package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.alahijani.lf.psi.api.TypeKeyword;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TypeKeywordImpl extends TwelfElementImpl implements TypeKeyword {
    public TypeKeywordImpl(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public TypeKeywordImpl(@NotNull ASTNode node) {
        super(node);
    }
}
