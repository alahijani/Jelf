package org.alahijani.lf.psi.xref;

import com.intellij.find.impl.HelpID;
import com.intellij.lang.LangBundle;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.formatter.TwelfFormatter;
import org.alahijani.lf.psi.api.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfFindUsagesProvider implements FindUsagesProvider {

    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof LfDeclaration ||
                psiElement instanceof TwelfFile;
    }

    public String getHelpId(@NotNull PsiElement psiElement) {
        return HelpID.FIND_IN_PROJECT;
    }

    @NotNull
    public String getType(@NotNull PsiElement element) {
        if (element instanceof PsiFile) {
            return LangBundle.message("terms.file");
        }
        if (element instanceof LfLocalVariable) {
            return TwelfBundle.message("terms.declaration.local");
        }
        if (element instanceof LfGlobalVariable) {
            return TwelfBundle.message("terms.declaration.global");
        }
        if (element instanceof LfMetaVariable) {
            return TwelfBundle.message("terms.declaration.meta");
        }

        return "";
    }

    @NotNull
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof PsiFile) {
            return ((PsiFile) element).getName();
        }
        if (element instanceof LfDeclaration) {
            return ((LfDeclaration) element).getName();
        }

        return "";
    }

    @NotNull
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof PsiFile) {
            return ((PsiFile) element).getName();
        }
        if (element instanceof LfDeclaration) {
            LfDeclaration declaration = (LfDeclaration) element;
            String text = declaration.getName();
            LfTerm type = declaration.getType();
            text += ": " + TwelfFormatter.format(type);
            return text;
        }

        return "";
    }

    public WordsScanner getWordsScanner() {
        return null;
    }
}
