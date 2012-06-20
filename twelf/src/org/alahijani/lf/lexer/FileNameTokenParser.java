package org.alahijani.lf.lexer;

import org.alahijani.lf.lang.TwelfConfigTokenType;

/**
 * @author Ali Lahijani
 */
public class FileNameTokenParser extends com.intellij.ide.highlighter.custom.tokens.IdentifierParser {

    public FileNameTokenParser() {
    }

    public boolean hasToken(int position) {
        if (!isPart(myBuffer.charAt(position))) return false;
        final int start = position;
        for (position++; position < myEndOffset; position++) {
            final char c = myBuffer.charAt(position);
            if (!isPart(c)) break;
        }
        myTokenInfo.updateData(start, position, TwelfConfigTokenType.FILENAME);
        return true;
    }

    protected static boolean isPart(char ch) {
        return !TwelfLexer.isWhitespace(ch) && ch != '/' && ch != ':';
    }

}
