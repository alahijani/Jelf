package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiFile;

/**
 * @author Ali Lahijani
 */
public interface TwelfConfigFile extends PsiFile, TwelfConfigElement {

    TwelfIdentifierReference[] getMemberFiles();

    boolean canReference(TwelfFile twelf, TwelfFile containingFile);
}
