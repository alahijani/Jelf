package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface ApplicationExpression extends LfTerm {
    LfTerm getFunction();

    LfTerm getArgument();
}
