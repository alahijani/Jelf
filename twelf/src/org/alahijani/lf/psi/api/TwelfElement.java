package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiInvalidElementAccessException;

/**
 * @author Ali Lahijani
 */
public interface TwelfElement extends TwelfBaseElement {

    TwelfFile getContainingFile() throws PsiInvalidElementAccessException;

}
