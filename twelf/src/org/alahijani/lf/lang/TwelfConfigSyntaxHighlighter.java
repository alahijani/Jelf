package org.alahijani.lf.lang;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.editor.TwelfHighlighterColors;
import org.alahijani.lf.lexer.TwelfConfigLexer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> ourMap;

    static {
        ourMap = new HashMap<IElementType, TextAttributesKey>();
        ourMap.put(TwelfConfigTokenType.END_OF_LINE_COMMENT, TwelfHighlighterColors.LINE_COMMENT);
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return new TwelfConfigLexer();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return new TextAttributesKey[] {ourMap.get(tokenType)};
    }

    public TwelfConfigSyntaxHighlighter() {
    }
}
