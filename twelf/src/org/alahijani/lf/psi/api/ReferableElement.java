package org.alahijani.lf.psi.api;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public interface ReferableElement extends TwelfElement {
    @NotNull
    String getName();

    String getTypeText();

}
