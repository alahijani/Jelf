package org.alahijani.lf.fileTypes;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.alahijani.lf.lang.Twelf;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.editor.TwelfEditorHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Ali Lahijani
 */
public class TwelfFileType extends LanguageFileType {

    public static final TwelfFileType INSTANCE = new TwelfFileType();

    private TwelfFileType() {
        super(Twelf.INSTANCE);
    }

    @NotNull
    public String getName() {
        return "Twelf";
    }

    @NotNull
    public String getDescription() {
        return "Twelf files";
    }

    @NotNull
    public String getDefaultExtension() {
        return "elf";
    }

    public Icon getIcon() {
        return TwelfIcons.TWELF_FILE;
    }

    @Override
    public boolean isJVMDebuggingSupported() {
        return false;
    }

//    @Override
//    public EditorHighlighter getEditorHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme colors) {
//        return new TwelfEditorHighlighter(colors, project, virtualFile);
//    }
}
