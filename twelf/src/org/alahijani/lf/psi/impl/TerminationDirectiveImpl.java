package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.lang.TwelfTokenType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TerminationDirectiveImpl extends TwelfDirectiveImpl {
    public TerminationDirectiveImpl(@NotNull ASTNode node) {
        super(node);

        String directiveName = getDirectiveName();
        assert directiveName.equals(TwelfTokenType.D_TOTAL)
            || directiveName.equals(TwelfTokenType.D_TERMINATES);
    }
}
