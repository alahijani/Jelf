package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface MetaVariableBinder extends LfTerm {
    LfDeclaration declareMeta(String name);

    LfDeclaration[] getMetaVariables();
}
