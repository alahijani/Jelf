package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface TypedLfTerm extends WrappingTerm {
    LfTerm getWrapped();

    LfTerm getType();
}
