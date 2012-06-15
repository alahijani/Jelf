package org.alahijani.lf.psi.api;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Ali Lahijani
 */
public interface TwelfStatement extends TwelfElement {
    @NotNull
    TwelfFile getFile();

    Map<String, LfDeclaration> getGlobalVariablesBefore();
}
