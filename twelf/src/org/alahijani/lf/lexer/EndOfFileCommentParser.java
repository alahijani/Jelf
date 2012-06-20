package org.alahijani.lf.lexer;

import com.intellij.ide.highlighter.custom.tokens.PrefixedTokenParser;
import com.intellij.psi.CustomHighlighterTokenType;

/**
 * @author Ali Lahijani
 */
public class EndOfFileCommentParser extends PrefixedTokenParser {
    public EndOfFileCommentParser(String prefix) {
        super(prefix, CustomHighlighterTokenType.MULTI_LINE_COMMENT);
    }

    @Override
    protected int getTokenEnd(int position) {
        return myEndOffset;
    }
}
