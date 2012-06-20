package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface GlobalVariableBinder extends TwelfStatement, MetaVariableBinder {

    LfGlobalVariable getDeclaration();

}
