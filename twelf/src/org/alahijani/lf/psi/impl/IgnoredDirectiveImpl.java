package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class IgnoredDirectiveImpl extends TwelfDirectiveImpl {
    public IgnoredDirectiveImpl(@NotNull ASTNode node) {
        super(node);
    }
}
