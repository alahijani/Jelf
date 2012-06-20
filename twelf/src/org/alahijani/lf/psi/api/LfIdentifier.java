package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface LfIdentifier extends LfElement {

    String getText();

    void setText(String text);

    boolean isUppercase();

    boolean isAnonymous();
}
