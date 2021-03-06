package org.alahijani.lf.psi.api;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public interface MetaVariableBinder extends LfTerm {

    @NotNull
    LfMetaVariable getProvisionalMeta(String name);

}
