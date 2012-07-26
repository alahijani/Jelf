package org.alahijani.lf.psi.api;

/**
 * A term that is functionally equivalent to its {@link #getWrapped() wrapped} content.
 *
 * @author Ali Lahijani
 */
public interface WrappingTerm extends LfTerm {
    LfTerm getWrapped();
}
