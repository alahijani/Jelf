package org.alahijani.lf.lang;

import com.intellij.psi.CustomHighlighterTokenType;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.alahijani.lf.psi.tree.ITwelfElementType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public interface TwelfTokenType extends TokenType {

    IElementType WHITESPACE = CustomHighlighterTokenType.WHITESPACE;
    IElementType END_OF_LINE_COMMENT = CustomHighlighterTokenType.LINE_COMMENT;     //new ITwelfElementType("END_OF_LINE_COMMENT");
    IElementType BLOCK_COMMENT = CustomHighlighterTokenType.MULTI_LINE_COMMENT;     //new ITwelfElementType("BLOCK_COMMENT");
    IElementType DOC_COMMENT = CustomHighlighterTokenType.MULTI_LINE_COMMENT;       //new ITwelfElementType("DOC_COMMENT");

    IElementType INTEGER_LITERAL = CustomHighlighterTokenType.NUMBER;   //new ITwelfElementType("INTEGER_LITERAL");
    IElementType RATIONAL_LITERAL = CustomHighlighterTokenType.NUMBER;  //new ITwelfElementType("RATIONAL_LITERAL");
    IElementType STRING_LITERAL = new ITwelfElementType("STRING_LITERAL");

    IElementType IDENT = new ITwelfElementType("IDENT");
    IElementType KEYWORD = new ITwelfElementType("KEYWORD");
    IElementType DIRECTIVE = new ITwelfElementType("DIRECTIVE");

    TokenSet DIRECTIVE_TOKEN_SET = TokenSet.create(DIRECTIVE);

    IElementType LPARENTH = new ITwelfElementType("LPARENTH");
    IElementType RPARENTH = new ITwelfElementType("RPARENTH");
    IElementType LBRACE = new ITwelfElementType("LBRACE");
    IElementType RBRACE = new ITwelfElementType("RBRACE");
    IElementType LBRACKET = new ITwelfElementType("LBRACKET");
    IElementType RBRACKET = new ITwelfElementType("RBRACKET");

    IElementType DOT = new ITwelfElementType("DOT");
    IElementType COLON = new ITwelfElementType("COLON");

    IElementType PLACEHOLDER = new ITwelfElementType("PLACEHOLDER");

    TokenSet APPLICATION_CANDIDATE = TokenSet.create(IDENT, LPARENTH, LBRACKET, STRING_LITERAL, INTEGER_LITERAL, RATIONAL_LITERAL);

    TokenSet OPERATION_BIT_SET = TokenSet.create();

    TokenSet STRING_LITERALS = TokenSet.create(STRING_LITERAL);

    TokenSet WHITESPACE_BIT_SET = TokenSet.create(WHITE_SPACE, WHITESPACE);

    TokenSet COMMENT_BIT_SET = TokenSet.create(END_OF_LINE_COMMENT, BLOCK_COMMENT, DOC_COMMENT);

    TokenSet WHITE_SPACE_OR_COMMENT_BIT_SET = TokenSet.create(WHITE_SPACE, WHITESPACE, END_OF_LINE_COMMENT, BLOCK_COMMENT, DOC_COMMENT);

    String KEYWORD_TO_ARROW = "->";
    String KEYWORD_FROM_ARROW = "<-";
    String KEYWORD_EQ = "=";
    String KEYWORD_TYPE = "type";
    String[] KEYWORDS = new String[]{
            KEYWORD_TO_ARROW,
            KEYWORD_FROM_ARROW,
            KEYWORD_EQ,
            KEYWORD_TYPE,
    };

    String D_INFIX = "%infix";
    String D_PREFIX = "%prefix";
    String D_POSTFIX = "%postfix";
    String D_NAME = "%name";
    String D_DEFINE = "%define";                /* -rv 8/27/01 */
    String D_SOLVE = "%solve";
    String D_QUERY = "%query";
    String D_FQUERY = "%fquery";
    String D_COMPILE = "%compile";              /* -ABP 4/4/03 */
    String D_QUERYTABLED = "%querytabled";
    String D_MODE = "%mode";
    String D_UNIQUE = "%unique";
    String D_COVERS = "%covers";
    String D_TOTAL = "%total";
    String D_TERMINATES = "%terminates";
    String D_BLOCK = "%block";                  /* -cs 6/3/01. */
    String D_WORLDS = "%worlds";
    String D_REDUCES = "%reduces";              /*  -bp 6/5/99.   */
    String D_TABLED = "%tabled";                /*  -bp 20/11/01. */
    String D_KEEPTABLE = "%keepTable";          /*  -bp 04/11/03. */
    String D_THEOREM = "%theorem";
    String D_PROVE = "%prove";
    String D_ESTABLISH = "%establish";
    String D_ASSERT = "%assert";
    String D_ABBREV = "%abbrev";
    String D_TRUSTME = "%trustme";
    String D_SUBORD = "%subord";
    String D_FREEZE = "%freeze";
    String D_THAW = "%thaw";
    String D_DETERMINISTIC = "%deterministic";  /* -rv 11/27/01. */
    String D_CLAUSE = "%clause";                /* -fp 08/09/02 */
    String D_SIG = "%sig";
    String D_STRUCT = "%struct";
    String D_WHERE = "%where";
    String D_INCLUDE = "%include";
    String D_OPEN = "%open";
    String D_USE = "%use";

    String[] DIRECTIVES = new String[]{
            D_INFIX,
            D_PREFIX,
            D_POSTFIX,
            D_NAME,
            D_DEFINE,
            D_SOLVE,
            D_QUERY,
            D_FQUERY,
            D_COMPILE,
            D_QUERYTABLED,
            D_MODE,
            D_UNIQUE,
            D_COVERS,
            D_TOTAL,
            D_TERMINATES,
            D_BLOCK,
            D_WORLDS,
            D_REDUCES,
            D_TABLED,
            D_KEEPTABLE,
            D_THEOREM,
            D_PROVE,
            D_ESTABLISH,
            D_ASSERT,
            D_ABBREV,
            D_TRUSTME,
            D_SUBORD,
            D_FREEZE,
            D_THAW,
            D_DETERMINISTIC,
            D_CLAUSE,
            D_SIG,
            D_STRUCT,
            D_WHERE,
            D_INCLUDE,
            D_OPEN,
            D_USE,
    };

    Set<String> DIRECTIVE_SET = new HashSet<String>(Arrays.asList(DIRECTIVES));

}
