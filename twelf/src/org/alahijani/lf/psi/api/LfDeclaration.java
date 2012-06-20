package org.alahijani.lf.psi.api;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public interface LfDeclaration extends LfElement, NavigatablePsiElement, PsiNamedElement {

    @NotNull
    String getName();

    LfIdentifier getNameIdentifier();

    LfTerm getType();

    LfTypeElement getTypeElement();

}