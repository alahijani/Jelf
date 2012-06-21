package org.alahijani.lf.psi.api;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNamedElement;

/**
 * @author Ali Lahijani
 */
public interface LfDeclaration extends ReferableElement, NavigatablePsiElement, PsiNamedElement {

    TwelfIdentifier getNameIdentifier();

    LfTerm getType();

    LfTypeElement getTypeElement();

}