package org.alahijani.lf.psi.api;

import org.jetbrains.annotations.Nullable;

/**
 * @author Ali Lahijani
 */
public interface LocalVariableBinder extends LfTerm {
    @Nullable LfLocalVariable getBoundDeclaration();

    LfTerm getBody();
}
