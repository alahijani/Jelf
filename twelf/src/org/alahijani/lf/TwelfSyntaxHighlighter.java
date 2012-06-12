package org.alahijani.lf;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.editor.TwelfHighlighterColors;
import org.alahijani.lf.lexer.TwelfLexer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ali Lahijani
 */
public class TwelfSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> ourMap1;
    private static final Map<IElementType, TextAttributesKey> ourMap2;

    static {
        ourMap1 = new HashMap<IElementType, TextAttributesKey>();
        ourMap2 = new HashMap<IElementType, TextAttributesKey>();

        fillMap(ourMap1, TwelfTokenType.OPERATION_BIT_SET, TwelfHighlighterColors.OPERATION_SIGN);

        ourMap1.put(TwelfTokenType.KEYWORD, TwelfHighlighterColors.LF_KEYWORD);
        ourMap1.put(TwelfTokenType.DIRECTIVE, TwelfHighlighterColors.TWELF_KEYWORD);

        ourMap1.put(TwelfTokenType.BLOCK_COMMENT, TwelfHighlighterColors.TWELF_BLOCK_COMMENT);
        ourMap1.put(TwelfTokenType.END_OF_LINE_COMMENT, TwelfHighlighterColors.LINE_COMMENT);
        ourMap1.put(TwelfTokenType.DOC_COMMENT, TwelfHighlighterColors.DOC_COMMENT);
        ourMap1.put(TwelfTokenType.INTEGER_LITERAL, TwelfHighlighterColors.INTEGER);
        ourMap1.put(TwelfTokenType.RATIONAL_LITERAL, TwelfHighlighterColors.RATIONAL);
        ourMap1.put(TwelfTokenType.STRING_LITERAL, TwelfHighlighterColors.STRING);

        ourMap1.put(TwelfTokenType.LPARENTH, TwelfHighlighterColors.PARENTHS);
        ourMap1.put(TwelfTokenType.RPARENTH, TwelfHighlighterColors.PARENTHS);

        ourMap1.put(TwelfTokenType.LBRACE, TwelfHighlighterColors.BRACES);
        ourMap1.put(TwelfTokenType.RBRACE, TwelfHighlighterColors.BRACES);

        ourMap1.put(TwelfTokenType.LBRACKET, TwelfHighlighterColors.BRACKETS);
        ourMap1.put(TwelfTokenType.RBRACKET, TwelfHighlighterColors.BRACKETS);

        ourMap1.put(TwelfTokenType.COLON, TwelfHighlighterColors.COLON);
        ourMap1.put(TwelfTokenType.DOT, TwelfHighlighterColors.DOT);
        //ourMap1.put(TwelfTokenType.COMMA, TwelfHighlighterColors.COMMA);
        //ourMap1.put(TwelfTokenType.SEMICOLON, TwelfHighlighterColors.SEMICOLON);
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return new TwelfLexer();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(ourMap1.get(tokenType), ourMap2.get(tokenType));
    }

    public TwelfSyntaxHighlighter() {
    }
}
