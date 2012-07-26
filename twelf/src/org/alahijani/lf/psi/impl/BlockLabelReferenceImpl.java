package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ResolveResult;
import org.alahijani.lf.psi.api.BlockLabel;
import org.alahijani.lf.psi.api.BlockLabelReference;
import org.alahijani.lf.psi.api.TwelfFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class BlockLabelReferenceImpl extends TwelfIdentifierReferenceImpl<BlockLabel> implements BlockLabelReference {
    public BlockLabelReferenceImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public ResolveResult[] doMultiResolve() {
        return new ResolveResult[0];
    }

    @Override
    public TwelfFile getContainingFile() {
        return (TwelfFile) super.getContainingFile();
    }
}
