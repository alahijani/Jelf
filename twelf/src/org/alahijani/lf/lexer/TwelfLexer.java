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
        parsers.add(new LineCommentParser("%%", false));
        parsers.add(new LineCommentParser("%\t", false));
        parsers.add(new LineCommentParser("% ", false));                    // todo maybe % followed by any whitespace?
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

    public void rethrow(Throwable e) {
             if (e instanceof Error) throw ((Error) e);
        else if (e instanceof RuntimeException) throw ((RuntimeException) e);
        else throw new AssertionError(e);
    }

    public <E1 extends Throwable>
    void rethrow(Throwable e,
                 Class<E1> expected1) throws E1 {
             if (e instanceof Error) throw ((Error) e);
        else if (e instanceof RuntimeException) throw ((RuntimeException) e);
        else if (expected1.isInstance(e)) throw expected1.cast(e);
        else throw new AssertionError(e);
    }

    public <E1 extends Throwable,
            E2 extends Throwable>
    void rethrow(Throwable e,
                 Class<E1> expected1,
                 Class<E2> expected2) throws E1, E2 {
             if (e instanceof Error) throw ((Error) e);
        else if (e instanceof RuntimeException) throw ((RuntimeException) e);
        else if (expected1.isInstance(e)) throw expected1.cast(e);
        else if (expected2.isInstance(e)) throw expected2.cast(e);
        else throw new AssertionError(e);
    }

    public <E1 extends Throwable,
            E2 extends Throwable,
            E3 extends Throwable>
    void rethrow(Throwable e,
                 Class<E1> expected1,
                 Class<E2> expected2,
                 Class<E3> expected3) throws E1, E2, E3 {
             if (e instanceof Error) throw ((Error) e);
        else if (e instanceof RuntimeException) throw ((RuntimeException) e);
        else if (expected1.isInstance(e)) throw expected1.cast(e);
        else if (expected2.isInstance(e)) throw expected2.cast(e);
        else if (expected3.isInstance(e)) throw expected3.cast(e);
        else throw new AssertionError(e);
    }
}
