package org.alahijani.lf.lang;

import com.intellij.psi.CustomHighlighterTokenType;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.alahijani.lf.psi.tree.ITwelfElementType;


public interface TwelfConfigTokenType extends TokenType {

    IElementType WHITESPACE = CustomHighlighterTokenType.WHITESPACE;
    IElementType END_OF_LINE_COMMENT = CustomHighlighterTokenType.LINE_COMMENT;     //new ITwelfElementType("END_OF_LINE_COMMENT");

    IElementType FILENAME = new ITwelfElementType("FILENAME");

    TokenSet COMMENT_BIT_SET = TokenSet.create(END_OF_LINE_COMMENT);
    TokenSet WHITESPACE_BIT_SET = TokenSet.create(WHITE_SPACE, WHITESPACE);
    TokenSet WHITE_SPACE_OR_COMMENT_BIT_SET = TokenSet.create(WHITE_SPACE, WHITESPACE, END_OF_LINE_COMMENT);
}
