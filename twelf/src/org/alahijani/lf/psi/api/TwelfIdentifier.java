package org.alahijani.lf.psi.api;

/**
 * A <code>TwelfIdentifier</code> is nether a {@link TwelfElement} nor a
 * {@link TwelfConfigElement}, because it can appear in both types of files.
 *
 * @author Ali Lahijani
 */
public interface TwelfIdentifier extends TwelfBaseElement {

    String getText();

    void setText(String text);

    boolean isUppercase();

    boolean isAnonymous();
}
