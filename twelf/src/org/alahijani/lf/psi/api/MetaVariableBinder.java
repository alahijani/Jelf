package org.alahijani.lf.psi.api;

/**
 * @author Ali Lahijani
 */
public interface MetaVariableBinder extends LfTerm {
    LfMetaVariable getMeta(String name);

    LfMetaVariable declareMeta(String name);

}
