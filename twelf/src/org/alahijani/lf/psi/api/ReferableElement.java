package org.alahijani.lf.psi.api;

import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public interface ReferableElement extends TwelfElement {
    @NotNull
    String getName();

    String getTypeText();

    @NotNull
    public SearchScope getUseScope();
}
