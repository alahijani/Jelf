package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface NameDirective extends TwelfStatement {
    TwelfIdentifierReference getForType();

    TwelfIdentifier getNameIdentifier();

    TwelfIdentifier getSecondNameIdentifier();
}
