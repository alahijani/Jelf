package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ResolveResult;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.LfIdentifierReference;
import org.alahijani.lf.psi.api.TwelfFile;
import org.alahijani.lf.psi.xref.Referencing;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LfIdentifierReferenceImpl extends TwelfIdentifierReferenceImpl<LfDeclaration> implements LfIdentifierReference {
    public LfIdentifierReferenceImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public ResolveResult[] doMultiResolve() {
        return Referencing.multiResolveTarget(this);
    }

    @Override
    public TwelfFile getContainingFile() {
        return (TwelfFile) super.getContainingFile();
    }
}
