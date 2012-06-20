package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiReference;

/**
 * @author Ali Lahijani
 */
public interface LfIdentifierReference extends LfTerm, PsiReference
 //, PsiPolyVariantReference
 //, PsiQualifiedReference
{

    LfIdentifier getIdentifier();

}
