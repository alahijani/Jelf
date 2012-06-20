package org.alahijani.lf.lexer;

import com.intellij.ide.highlighter.custom.tokens.PrefixedTokenParser;
import com.intellij.psi.CustomHighlighterTokenType;


/**
 * A line no-comment is a (end-of-)line comment without a comment text. It is a Twelf idiosyncrasy
 * that a percent sign as the last character of a line is interpreted as a single character comment,
 * despite a single percent character not being a legitimate comment-starter in modern versions of
 * Twelf.
 *
 * @author Ali Lahijani
 */
public class LineNoCommentParser extends PrefixedTokenParser {
    int length;

    public LineNoCommentParser(String line) {
        super(line + "\n", CustomHighlighterTokenType.LINE_COMMENT);
        length = line.length();
    }

    protected int getTokenEnd(int position) {
        return position + length - 1;
    }

}
