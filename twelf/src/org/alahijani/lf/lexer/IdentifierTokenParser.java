package org.alahijani.lf.lexer;

import com.intellij.psi.PsiBundle;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.lang.TwelfTokenType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ali Lahijani
 */
public class IdentifierTokenParser extends com.intellij.ide.highlighter.custom.tokens.IdentifierParser {

    private List<String> keywords = Arrays.asList(TwelfTokenType.KEYWORDS);

    public IdentifierTokenParser() {
    }

    public boolean hasToken(int position) {
        if (!isIdentifierStart(myBuffer.charAt(position))) return false;
        final int start = position;
        for (position++; position < myEndOffset; position++) {
            final char c = myBuffer.charAt(position);
            if (!isIdentifierPart(c)) break;
        }
        String token = myBuffer.subSequence(start, position).toString();
        IElementType tokenType = getElementType(token);
        myTokenInfo.updateData(start, position, tokenType);
        return true;
    }

    private IElementType getElementType(String token) {
        IElementType tokenType;
        if (keywords.contains(token)) {
            tokenType = TwelfTokenType.KEYWORD;
        } else if (token.charAt(0) == '%') {
            tokenType = TwelfTokenType.DIRECTIVE;
        } else {
            tokenType = TwelfTokenType.IDENT;
        }
        return tokenType;
    }

    private boolean isIdentifierStart(char ch) {
        return isIdentifierPart(ch) || ch == '%';
    }

    protected boolean isIdentifierPart(char ch) {
        return !TwelfLexer.isReserved(ch) && !TwelfLexer.isWhitespace(ch);
    }

    public static void checkIsIdentifier(String text) {
        if (!isIdentifier(text)) {
            throw new IncorrectOperationException(PsiBundle.message("0.is.not.an.identifier", text));
        }
    }

    private static IdentifierTokenParser parser = new IdentifierTokenParser();

    public static boolean isIdentifier(String text) {
        return parser.isIdentifierText(text);
    }

    public boolean isIdentifierText(String text) {
        if (text.isEmpty()) {
            return false;
        }
        if (!isIdentifierStart(text.charAt(0))) {
            return false;
        }

        char[] charArray = text.toCharArray();
        for (int i = 1, charArrayLength = charArray.length; i < charArrayLength; i++) {
            if (!isIdentifierPart(charArray[i])) {
                return false;
            }
        }

        return true;
    }
}
