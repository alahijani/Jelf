package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ali Lahijani
 */
public interface TwelfIdentifierReference<Referable extends ReferableElement>
        extends PsiElement, PsiReference, PsiPolyVariantReference /*, PsiQualifiedReference*/ {

    TwelfIdentifier getIdentifier();

    @Nullable
    Referable resolve();

}
