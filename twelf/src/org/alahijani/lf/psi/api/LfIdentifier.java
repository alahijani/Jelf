package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiType;

/**
 * @author Ali Lahijani
 */
public interface LfIdentifier extends LfElement {
    PsiType getType();

    String getText();

    void setText(String text);

    boolean isUppercase();

    boolean isAnonymous();
}
