package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface PiType extends LocalVariableBinder {
    LfTerm getParameterType();

    LfTerm getReturnType();
}
