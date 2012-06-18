package org.alahijani.lf.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.alahijani.lf.Twelf;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.fileTypes.TwelfFileType;
import org.alahijani.lf.psi.api.TwelfFile;
import org.alahijani.lf.psi.api.TwelfStatement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
}
