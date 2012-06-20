package org.alahijani.lf.lexer;

import com.intellij.ide.highlighter.custom.AbstractCustomLexer;
import com.intellij.ide.highlighter.custom.tokens.*;
import org.alahijani.lf.lang.TwelfTokenType;

import java.util.*;

/**
 * @author Ali Lahijani
 */
public class TwelfLexer extends AbstractCustomLexer {

    public TwelfLexer() {
        super(getParsers());
    }

    private static ArrayList<TokenParser> getParsers() {
        ArrayList<TokenParser> parsers = new ArrayList<TokenParser>();

        parsers.add(new WhitespaceParser());
        parsers.add(LineCommentParser.create("%%"));
        parsers.add(LineCommentParser.create("%\t"));
        parsers.add(LineCommentParser.create("% "));                    // todo maybe % followed by any whitespace?
        parsers.add(new LineNoCommentParser("%"));
        parsers.add(MultilineCommentParser.create("%{", "}%"));
        parsers.add(new EndOfFileCommentParser("%."));

        parsers.add(new IdentifierTokenParser());
        parsers.add(new QuotedStringParser("\"", TwelfTokenType.STRING_LITERAL, false));

        parsers.add(new BraceTokenParser(".", TwelfTokenType.DOT));
        parsers.add(new BraceTokenParser(":", TwelfTokenType.COLON));

        parsers.add(new BraceTokenParser("{", TwelfTokenType.LBRACE));
        parsers.add(new BraceTokenParser("}", TwelfTokenType.RBRACE));

        parsers.add(new BraceTokenParser("[", TwelfTokenType.LBRACKET));
        parsers.add(new BraceTokenParser("]", TwelfTokenType.RBRACKET));

        parsers.add(new BraceTokenParser("(", TwelfTokenType.LPARENTH));
        parsers.add(new BraceTokenParser(")", TwelfTokenType.RPARENTH));

        return parsers;
    }

    static boolean isWhitespace(char ch) {
        return (ch == ' ') ||           // space
                (ch == '\n') ||         // newline
                (ch == '\t') ||         // tab
                (ch == '\r') ||         // carriage return
                (ch == '\u000B') ||     // vertical tab
                (ch == '\u000C');       // formfeed
    }

    static boolean isReserved(char ch) {
        return (ch == '.') ||
                (ch == ':') ||
                (ch == '%') ||
                (ch == '"') ||
                (ch == '(') || (ch == ')') ||
                (ch == '{') || (ch == '}') ||
                (ch == '[') || (ch == ']');
    }

    public static boolean isUppercaseIdentifier(String text) {
        if (text.isEmpty()) {
            return false;
        }
        char ch = text.charAt(0);
        return isUppercase(ch);
    }

    public static boolean isUppercase(char ch) {
        return ('A' <= ch && ch <= 'Z') || ch == '_';
    }

    public static boolean isAnonymousIdentifier(String text) {
        return "_".equals(text);
    }
}
