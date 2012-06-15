package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface MetaVariableBinder extends LfTerm {
    LfDeclaration getMeta(String name);

    LfDeclaration declareMeta(String name);

}
