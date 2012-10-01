package org.alahijani.lf.lexer;

import com.intellij.ide.highlighter.custom.AbstractCustomLexer;
import com.intellij.ide.highlighter.custom.tokens.LineCommentParser;
import com.intellij.ide.highlighter.custom.tokens.TokenParser;
import com.intellij.ide.highlighter.custom.tokens.WhitespaceParser;

import java.util.ArrayList;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigLexer extends AbstractCustomLexer {

    public TwelfConfigLexer() {
        super(getParsers());
    }

    private static ArrayList<TokenParser> getParsers() {
        ArrayList<TokenParser> parsers = new ArrayList<TokenParser>();

        parsers.add(new WhitespaceParser());
        parsers.add(new LineCommentParser("%", false));
        parsers.add(new FileNameTokenParser());

        return parsers;
    }

}
