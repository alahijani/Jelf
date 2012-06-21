package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiInvalidElementAccessException;

/**
 * @author Ali Lahijani
 */
public interface TwelfConfigElement extends TwelfBaseElement {

    TwelfConfigFile getContainingFile() throws PsiInvalidElementAccessException;

}
