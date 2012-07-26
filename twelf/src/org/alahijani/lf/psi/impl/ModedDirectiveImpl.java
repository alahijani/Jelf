package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.lang.TwelfTokenType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class ModedDirectiveImpl extends TwelfDirectiveImpl {
    public ModedDirectiveImpl(@NotNull ASTNode node) {
        super(node);

        String directiveName = getDirectiveName();
        assert directiveName.equals(TwelfTokenType.D_MODE)
            || directiveName.equals(TwelfTokenType.D_COVERS)
            || directiveName.equals(TwelfTokenType.D_UNIQUE);
    }
}
