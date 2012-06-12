package org.alahijani.lf.psi.api;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public interface TwelfStatement extends TwelfElement {
    @NotNull
    TwelfFile getFile();

}
