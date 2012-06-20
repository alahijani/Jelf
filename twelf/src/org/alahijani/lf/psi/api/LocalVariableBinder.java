package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface LocalVariableBinder extends LfTerm {
    LfLocalVariable getBoundDeclaration();
}
