package org.alahijani.lf.psi.impl;

import com.intellij.codeInsight.PsiEquivalenceUtil;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.fileTypes.TwelfConfigFileType;
import org.alahijani.lf.lang.TwelfConfig;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.util.TwelfPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigFileImpl extends PsiFileBase implements TwelfConfigFile {
    public TwelfConfigFileImpl(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TwelfConfig.INSTANCE);
    }

    @NotNull
    public FileType getFileType() {
        return TwelfConfigFileType.INSTANCE;
    }

    @Override
    public Icon getIcon(int flags) {
        return TwelfIcons.TWELF_CONFIG_FILE;
    }

    public TwelfFileReference[] getMemberFiles() {
        return findChildrenByClass(TwelfFileReference.class);
    }

    public boolean canReference(TwelfFile user, TwelfFile declaration) {
        boolean declared = false;
        for (TwelfIdentifierReference member : getMemberFiles()) {
            if (!declared && member.getCanonicalText().equals(declaration.getName())) {
                declared = true;
            }
            if (member.getCanonicalText().equals(user.getName())) {
                return declared;
            }
        }

        throw new IllegalArgumentException("Not a member file: " + user.getName());
    }

    public Collection<LfGlobalVariable> getAllDeclarations(String name, TwelfFile beforeFile) {
        TwelfFileReference[] memberFiles = getMemberFiles();
        ArrayList<LfGlobalVariable> result = new ArrayList<LfGlobalVariable>();
        for (TwelfFileReference member : memberFiles) {
            TwelfFile file = member.resolve();
            if (file == null) {
                continue;
            }
            if (PsiEquivalenceUtil.areElementsEquivalent(file, beforeFile)) {
                break;
            }
            result.addAll(file.getAllDeclarations(name, Integer.MAX_VALUE));
        }
        return result;
    }

    public LfGlobalVariable getLastDeclaration(String name, TwelfFile beforeFile) {
        return getLastDeclaration(name, beforeFile, null);
    }

    public LfGlobalVariable getLastDeclaration(String name, TwelfFile beforeFile, @Nullable CodeInsightsHolder callback) {
        TwelfFileReference[] memberFiles = getMemberFiles();
        LfGlobalVariable result = null;
        for (TwelfFileReference member : memberFiles) {
            TwelfFile file = member.resolve();
            if (file == null) {
                continue;
            }
            if (PsiEquivalenceUtil.areElementsEquivalent(file, beforeFile)) {
                break;
            }
            LfGlobalVariable lastDeclaration = file.getLastDeclaration(name, Integer.MAX_VALUE);
            if (lastDeclaration != null) {
                if (result != null && callback != null) {
                    callback.hidesOtherDeclaration(lastDeclaration, this);
                }
                result = lastDeclaration;
            }
        }
        return result;
    }

    public TwelfBaseElement getVirtualParent() {
        return null;
    }

    public Iterable<? extends TwelfBaseElement> getVirtualParentChain() {
        return TwelfPsiUtil.getVirtualParentChain(this);
    }

}
