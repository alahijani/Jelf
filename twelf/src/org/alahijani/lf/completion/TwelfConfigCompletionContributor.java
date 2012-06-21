package org.alahijani.lf.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.alahijani.lf.psi.api.TwelfBaseElement;
import org.alahijani.lf.psi.api.TwelfFile;
import org.alahijani.lf.psi.xref.LfLookupItem;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigCompletionContributor extends CompletionContributor {

    public TwelfConfigCompletionContributor() {
        extend(CompletionType.BASIC, psiElement(PsiElement.class), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          ProcessingContext context,
                                          @NotNull final CompletionResultSet result) {

                final PsiElement position = parameters.getPosition();
                final PsiElement reference = position.getParent();

                if (reference instanceof TwelfBaseElement) {
                    PsiDirectory directory = parameters.getOriginalFile().getContainingDirectory();
                    if (directory != null) {
                        for (PsiFile file : directory.getFiles()) {
                            if (file instanceof TwelfFile) {
                                result.addElement(new LfLookupItem((TwelfFile) file));
                            }
                        }
                    }
                }
            }
        });
    }

}
