package org.alahijani.lf.fileTypes;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.alahijani.lf.TwelfConfig;
import org.alahijani.lf.TwelfIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigFileType extends LanguageFileType {

    public static final TwelfConfigFileType INSTANCE = new TwelfConfigFileType();

    private TwelfConfigFileType() {
        super(TwelfConfig.INSTANCE);
    }

    @NotNull
    public String getName() {
        return "TwelfConfig";
    }

    @NotNull
    public String getDescription() {
        return "Twelf sources configuration files";
    }

    @NotNull
    public String getDefaultExtension() {
        return "cfg";
    }

    public Icon getIcon() {
        return TwelfIcons.TWELF_CONFIG_FILE;
    }

    @Override
    public boolean isJVMDebuggingSupported() {
        return false;
    }

/*
    @Override
    public EditorHighlighter getEditorHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme colors) {
        return new TwelfEditorHighlighter(colors, project, virtualFile);
    }
*/
}
