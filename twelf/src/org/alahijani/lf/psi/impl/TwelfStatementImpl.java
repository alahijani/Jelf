package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.TwelfFile;
import org.alahijani.lf.psi.api.TwelfStatement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfStatementImpl extends TwelfElementImpl implements TwelfStatement {

    public TwelfStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public TwelfFile getFile() {
        return (TwelfFile) getParent();
    }


}
