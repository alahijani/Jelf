package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface NameDirective extends TwelfStatement {
    LfIdentifierReference getForType();

    LfIdentifier getNameIdentifier();

    LfIdentifier getSecondNameIdentifier();
}
