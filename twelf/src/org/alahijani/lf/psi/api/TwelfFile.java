package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public interface TwelfFile extends PsiFile {
    @NotNull TwelfStatement[] getStatements();
}
