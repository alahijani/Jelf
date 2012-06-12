package org.alahijani.lf.editor;

import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;

import java.awt.*;

/**
 * @author yole
 */
public class TwelfHighlighterColors {
    private TwelfHighlighterColors() {
    }

    public static final TextAttributesKey DOC_COMMENT = TextAttributesKey.createTextAttributesKey("TWELF_DOC_COMMENT", SyntaxHighlighterColors.DOC_COMMENT.getDefaultAttributes());
    public static final TextAttributesKey LINE_COMMENT = TextAttributesKey.createTextAttributesKey("TWELF_LINE_COMMENT", SyntaxHighlighterColors.LINE_COMMENT.getDefaultAttributes());
    public static final TextAttributesKey TWELF_BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("TWELF_BLOCK_COMMENT", SyntaxHighlighterColors.JAVA_BLOCK_COMMENT.getDefaultAttributes());

    private static final TextAttributes DIRECTIVE_ATTRIBUTES = SyntaxHighlighterColors.KEYWORD.getDefaultAttributes().clone();

    static {
        DIRECTIVE_ATTRIBUTES.setForegroundColor(new Color(102, 14, 122));
        DIRECTIVE_ATTRIBUTES.setFontType(Font.BOLD);
        // CustomHighlighterColors.CUSTOM_KEYWORD3_ATTRIBUTES.getDefaultAttributes()
    }

    public static final TextAttributesKey LF_KEYWORD = TextAttributesKey.createTextAttributesKey("LF_KEYWORD", SyntaxHighlighterColors.KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey TWELF_KEYWORD = TextAttributesKey.createTextAttributesKey("TWELF_KEYWORD", DIRECTIVE_ATTRIBUTES);
    public static final TextAttributesKey INTEGER = TextAttributesKey.createTextAttributesKey("TWELF_INTEGER", SyntaxHighlighterColors.NUMBER.getDefaultAttributes());
    public static final TextAttributesKey RATIONAL = TextAttributesKey.createTextAttributesKey("TWELF_RATIONAL", SyntaxHighlighterColors.NUMBER.getDefaultAttributes());
    public static final TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey("TWELF_STRING", SyntaxHighlighterColors.STRING.getDefaultAttributes());
    public static final TextAttributesKey OPERATION_SIGN = TextAttributesKey.createTextAttributesKey("TWELF_OPERATION_SIGN", SyntaxHighlighterColors.OPERATION_SIGN.getDefaultAttributes());
    public static final TextAttributesKey PARENTHS = TextAttributesKey.createTextAttributesKey("TWELF_PARENTH", SyntaxHighlighterColors.PARENTHS.getDefaultAttributes());
    public static final TextAttributesKey BRACKETS = TextAttributesKey.createTextAttributesKey("TWELF_BRACKETS", SyntaxHighlighterColors.BRACKETS.getDefaultAttributes());
    public static final TextAttributesKey BRACES = TextAttributesKey.createTextAttributesKey("TWELF_BRACES", SyntaxHighlighterColors.BRACES.getDefaultAttributes());
    public static final TextAttributesKey COLON = TextAttributesKey.createTextAttributesKey("TWELF_COLON", SyntaxHighlighterColors.COMMA.getDefaultAttributes());
    public static final TextAttributesKey DOT = TextAttributesKey.createTextAttributesKey("TWELF_DOT", SyntaxHighlighterColors.DOT.getDefaultAttributes());
    public static final TextAttributesKey DOC_COMMENT_TAG = TextAttributesKey.createTextAttributesKey("TWELF_DOC_TAG", SyntaxHighlighterColors.DOC_COMMENT_TAG.getDefaultAttributes());
    public static final TextAttributesKey DOC_COMMENT_MARKUP = TextAttributesKey.createTextAttributesKey("TWELF_DOC_MARKUP", SyntaxHighlighterColors.DOC_COMMENT_MARKUP.getDefaultAttributes());

    private static final TextAttributes LOCAL_ATTRIBUTES = HighlightInfoType.LOCAL_VARIABLE.getAttributesKey().getDefaultAttributes().clone();

    static {
        LOCAL_ATTRIBUTES.setForegroundColor(new Color(122, 122, 43));
    }

    public static final TextAttributesKey LF_LOCAL_IDENTIFIER = TextAttributesKey.createTextAttributesKey("LF_LOCAL_IDENTIFIER", LOCAL_ATTRIBUTES);
    public static final TextAttributesKey LF_SIGNATURE_IDENTIFIER = TextAttributesKey.createTextAttributesKey("LF_SIGNATURE_IDENTIFIER", HighlightInfoType.METHOD_CALL.getAttributesKey().getDefaultAttributes());
    public static final TextAttributesKey LF_META_VARIABLE = TextAttributesKey.createTextAttributesKey("LF_META_VARIABLE", HighlightInfoType.STATIC_METHOD.getAttributesKey().getDefaultAttributes());


}
