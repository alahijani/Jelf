package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.psi.api.TwelfElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfElementImpl extends TwelfBaseElementImpl implements TwelfElement {

    public TwelfElementImpl(@NotNull ASTNode node) {
        super(node);
    }

}
