package org.alahijani.lf.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.fileTypes.TwelfConfigFileType;
import org.alahijani.lf.lang.TwelfConfig;
import org.alahijani.lf.psi.api.TwelfConfigFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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

}
