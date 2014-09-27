package org.alahijani.lf.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.NonClasspathDirectoriesScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FileBasedIndex;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.fileTypes.TwelfFileType;
import org.alahijani.lf.lang.Twelf;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.stubs.index.LfGlobalVariableIndex;
import org.alahijani.lf.psi.stubs.index.TwelfConfigFileFBIndex;
import org.alahijani.lf.psi.stubs.index.TwelfConfigFileIndex;
import org.alahijani.lf.psi.util.TwelfPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Ali Lahijani
 */
public class TwelfFileImpl extends PsiFileBase implements TwelfFile {
    public TwelfFileImpl(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, Twelf.INSTANCE);
    }

    @NotNull
    public FileType getFileType() {
        return TwelfFileType.INSTANCE;
    }

    @Override
    public Icon getIcon(int flags) {
        return TwelfIcons.TWELF_FILE;
    }

    @NotNull
    public TwelfStatement[] getStatements() {
        return findChildrenByClass(TwelfStatement.class);
    }

    public String getTypeText() {
        return TwelfBundle.message("terms.file.twelf");
    }

    public boolean getContainingConfigFiles(FileBasedIndex.ValueProcessor<Integer> processor) {
        return TwelfConfigFileFBIndex.getContainingConfigFiles(getName(), getScope(), processor);
    }

    public Collection<TwelfConfigFile> getContainingConfigFiles() {
        return TwelfConfigFileIndex.getContainingTwelfConfigFiles(getName(), getScope(), getProject());
    }

    private GlobalSearchScope getScope() {
        PsiDirectory directory = getContainingDirectory();
        return directory == null
                ? GlobalSearchScope.allScope(getProject())
                : new NonClasspathDirectoriesScope(Collections.singleton((directory.getVirtualFile())));
    }

    public boolean getAllDeclarations(Processor<LfGlobalVariable> processor) {
        // todo
        return true;
    }

    public Collection<LfGlobalVariable> getAllDeclarations(String name, int beforeOffset) {
        Collection<LfGlobalVariable> candidates
                = LfGlobalVariableIndex.getLfGlobalVariables(name, GlobalSearchScope.fileScope(this), getProject());

        if (beforeOffset == Integer.MAX_VALUE) return candidates;   // optimization

        for (Iterator<LfGlobalVariable> it = candidates.iterator(); it.hasNext(); ) {
            LfGlobalVariable candidate = it.next();
            if (beforeOffset <= candidate.getTextOffset()) {
                it.remove();
            }
        }

        return candidates;
    }

    public LfGlobalVariable getLastDeclaration(String name, int beforeOffset) {
        return getLastDeclaration(name, beforeOffset, null);
    }

    public LfGlobalVariable getLastDeclaration(String name, int beforeOffset, @Nullable CodeInsightsHolder callback) {
        Collection<LfGlobalVariable> candidates
                = LfGlobalVariableIndex.getLfGlobalVariables(name, GlobalSearchScope.fileScope(this), getProject());

        LfGlobalVariable best = null;
        int bestOffset = -1;

        for (LfGlobalVariable candidate : candidates) {
            int offset = candidate.getTextOffset();
            if (bestOffset < offset && offset < beforeOffset) {
                if (best != null && callback != null) {
                    callback.hidesOtherDeclaration(candidate);
                }
                best = candidate;
                bestOffset = offset;
            }
        }

        return best;
    }

    @NotNull
    @Override
    public SearchScope getUseScope() {
        return super.getUseScope();
    }

    public TwelfBaseElement getVirtualParent() {
        return null;
    }

    public Iterable<? extends TwelfBaseElement> getVirtualParentChain() {
        return TwelfPsiUtil.getVirtualParentChain(this);
    }
}
