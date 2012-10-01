package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiFile;

import java.util.Collection;

/**
 * @author Ali Lahijani
 */
public interface TwelfConfigFile extends PsiFile, TwelfConfigElement {

    TwelfFileReference[] getMemberFiles();

    boolean canReference(TwelfFile twelf, TwelfFile containingFile);

    Collection<LfGlobalVariable> getAllDeclarations(String name, TwelfFile beforeFile);

    LfGlobalVariable getLastDeclaration(String name, TwelfFile beforeFile);

    LfGlobalVariable getLastDeclaration(String name, TwelfFile beforeFile, CodeInsightsHolder callback);
}
