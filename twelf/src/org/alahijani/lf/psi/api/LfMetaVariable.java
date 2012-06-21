package org.alahijani.lf.psi.api;

/**
 * A (possibly provisional) LF meta-variable. Check {@link LfMetaVariable#isCommitted()} method for the
 * provisional/committed status.
 *
 * @author Ali Lahijani
 */
public interface LfMetaVariable extends LfDeclaration {

    boolean isCommitted();

    void commit();

}
