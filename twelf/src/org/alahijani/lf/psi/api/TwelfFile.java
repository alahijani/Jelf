package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiFile;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Ali Lahijani
 */
public interface TwelfFile extends PsiFile, ReferableElement {

    @NotNull
    TwelfStatement[] getStatements();

    Collection<TwelfConfigFile> getContainingConfigFiles();

    boolean getContainingConfigFiles(FileBasedIndex.ValueProcessor<Integer> processor);

    boolean getAllDeclarations(Processor<LfGlobalVariable> processor);

    Collection<LfGlobalVariable> getAllDeclarations(String name, int beforeOffset);

    LfGlobalVariable getLastDeclaration(String name, int beforeOffset);

    LfGlobalVariable getLastDeclaration(String name, int beforeOffset, CodeInsightsHolder callback);

}
