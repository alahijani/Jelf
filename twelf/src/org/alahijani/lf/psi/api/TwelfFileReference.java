package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiReference;

/**
 * @author Ali Lahijani
 */
public interface TwelfFileReference extends LfTerm, PsiReference
 /*, PsiQualifiedReference, PsiPolyVariantReference */ {

    TwelfFileName getFileName();

}
