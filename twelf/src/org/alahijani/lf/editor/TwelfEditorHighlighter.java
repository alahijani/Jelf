package org.alahijani.lf.editor;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.alahijani.lf.TwelfSyntaxHighlighter;

/**
 * @author Ali Lahijani
 */
public class TwelfEditorHighlighter extends LayeredLexerEditorHighlighter {
    public TwelfEditorHighlighter(EditorColorsScheme scheme, Project project, VirtualFile virtualFile) {
      super(new TwelfSyntaxHighlighter(), scheme);
//      registerTwelfdocHighlighter();
    }

/*
    private void registerTwelfdocHighlighter() {
      // Register TwelfDoc Highlighter
      SyntaxHighlighter groovyDocHighlighter = new TwelfDocSyntaxHighlighter();
      final LayerDescriptor groovyDocLayer = new LayerDescriptor(groovyDocHighlighter, "\n", DefaultHighlighter.DOC_COMMENT_CONTENT);
      registerLayer(TwelfDocElementTypes.GROOVY_DOC_COMMENT, groovyDocLayer);
    }
*/
}
