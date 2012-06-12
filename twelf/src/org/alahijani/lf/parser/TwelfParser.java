package org.alahijani.lf.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.TwelfElementType;
import org.alahijani.lf.TwelfTokenType;
import org.alahijani.lf.lexer.TwelfLexer;
import org.jetbrains.annotations.NotNull;

/**
 * Parser for Twelf script files
 *
 * @author Ali Lahijani
 */
public class TwelfParser implements PsiParser {

    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        builder.setDebugMode(true); // todo

        PsiBuilder.Marker document = builder.mark();

        while (!builder.eof()) {
            statement(builder);
        }

        document.done(root);
        return builder.getTreeBuilt();
    }

    public boolean statement(PsiBuilder builder) {

        if (builder.getTokenType() == TwelfTokenType.IDENT) {
            declarationStatement(builder);
        } else if (builder.getTokenType() == TwelfTokenType.DIRECTIVE) {
            directive(builder);
        } else {
            builder.error(TwelfBundle.message("expected.statement"));
            dot(builder);
        }

        return true;
    }

    private boolean lfDeclaration(PsiBuilder builder) {
        if (builder.getTokenType() != TwelfTokenType.IDENT) {
            return false;
        }

        PsiBuilder.Marker declaration = builder.mark();
        ParserUtils.eatElement(builder, TwelfElementType.IDENTIFIER);

        boolean colonOrEq = false;
        if (builder.getTokenType() == TwelfTokenType.COLON) {
            builder.advanceLexer();
            if (!term(builder)) {
                builder.error(TwelfBundle.message("expected.term"));
            }
            colonOrEq = true;
        }

        if (TwelfTokenType.KEYWORD == builder.getTokenType() && TwelfTokenType.KEYWORD_EQ.equals(builder.getTokenText())) {
            builder.advanceLexer();
            if (!term(builder)) {
                builder.error(TwelfBundle.message("expected.term"));
            }
            colonOrEq = true;
        }

        if (!colonOrEq) {
            builder.error(TwelfBundle.message("expected.colonOrEq"));
        }

        declaration.done(TwelfElementType.LF_DECLARATION);
        return true;

    }

    private boolean term(PsiBuilder builder) {
        PsiBuilder.Marker term = builder.mark();
        if (!primaryTerm(builder)) {
            term.drop();  // not needed for a single primary term
            return false;
        }

        do {
            IElementType elementType = null;
            if (TwelfTokenType.COLON == builder.getTokenType()) {
                elementType = TwelfElementType.TYPED_TERM;
            }
            if (TwelfTokenType.KEYWORD == builder.getTokenType() && TwelfTokenType.KEYWORD_TO_ARROW.equals(builder.getTokenText())) {
                elementType = TwelfElementType.TO_ARROW_TERM;
            }
            /**
             * todo: "<-" should be left-associative!
             */
            if (TwelfTokenType.KEYWORD == builder.getTokenType() && TwelfTokenType.KEYWORD_FROM_ARROW.equals(builder.getTokenText())) {
                elementType = TwelfElementType.FROM_ARROW_TERM;
            }
            if (elementType != null) {
                builder.advanceLexer();
                if (!term(builder)) {
                    builder.error(TwelfBundle.message("expected.type"));
                }
                term.done(elementType);

                /**
                 * Note that:
                 *    "B : C D"   is parsed as "B : (C D)"
                 *    "B -> C D"  is parsed as "B -> (C D)"
                 *    "B <- C D"  is parsed as "B <- (C D)"
                 * So we cannot consume more.
                 */
                return true;
            }

            /**
             * Note that:
             *    "A B C"     is parsed as "(A B) C"
             *    "A B : C"   is parsed as "(A B) : C"
             *    "A B -> C"  is parsed as "(A B) -> C"
             *    "A B <- C"  is parsed as "(A B) <- C"
             * You need parentheses if you mean "A (B C)" etc.
             * So arguments are primary terms, not arbitrary terms.
             */
            if (!primaryTerm(builder)) {
                term.drop();
                return true;
            }
            term.done(TwelfElementType.APPLICATION);
            term = term.precede();
        } while (true);
    }

    private boolean primaryTerm(PsiBuilder builder) {

        if (TwelfTokenType.KEYWORD == builder.getTokenType() && TwelfTokenType.KEYWORD_TYPE.equals(builder.getTokenText())) {
            ParserUtils.eatElement(builder, TwelfElementType.TYPE_KEYWORD);
            return true;
        }

        if (builder.getTokenType() == TwelfTokenType.IDENT) {
            return idReference(builder);
        }

        if (builder.getTokenType() == TwelfTokenType.STRING_LITERAL) {
            ParserUtils.eatElement(builder, TwelfElementType.STRING_EXPRESSION);
            return true;
        }

        if (builder.getTokenType() == TwelfTokenType.LPARENTH) {
            return parenthesizedTerm(builder);
        }

        if (builder.getTokenType() == TwelfTokenType.LBRACKET) {
            return binderTerm(builder, TwelfTokenType.LBRACKET, TwelfTokenType.RBRACKET, TwelfElementType.LAMBDA_TERM, TwelfBundle.message("expected.rBracket"));
        }

        if (builder.getTokenType() == TwelfTokenType.LBRACE) {
            return binderTerm(builder, TwelfTokenType.LBRACE, TwelfTokenType.RBRACE, TwelfElementType.PI_TERM, TwelfBundle.message("expected.rBrace"));
        }

        return false;
    }

    private boolean idReference(PsiBuilder builder) {
        if (builder.getTokenType() != TwelfTokenType.IDENT) {
            return false;
        }

        PsiBuilder.Marker idReference = builder.mark();
        ParserUtils.eatElement(builder, TwelfElementType.IDENTIFIER);
        idReference.done(TwelfElementType.REFERENCE_EXPRESSION);
        return true;
    }

    private boolean binderTerm(PsiBuilder builder,
                               IElementType lBracket,
                               IElementType rBracket,
                               IElementType elementType,
                               String rBracketExpected) {
        PsiBuilder.Marker bracketTerm = builder.mark();
        builder.advanceLexer();

        PsiBuilder.Marker paramDecl = builder.mark();
        if (builder.getTokenType() == TwelfTokenType.IDENT) {
            ParserUtils.eatElement(builder, TwelfElementType.IDENTIFIER);
            if (builder.getTokenType() == TwelfTokenType.COLON) {
                builder.advanceLexer();
                if (!term(builder)) {
                    builder.error(TwelfBundle.message("expected.type"));
                }
            }
            paramDecl.done(TwelfElementType.LF_DECLARATION);
            if (builder.getTokenType() == rBracket) {
                builder.advanceLexer();
            } else {
                builder.error(rBracketExpected);
            }
        } else {
            builder.error(TwelfBundle.message("expected.identifier"));
        }

        if (!term(builder)) {
            builder.error(TwelfBundle.message("expected.term"));
        }

        bracketTerm.done(elementType);
        return true;
    }

    public boolean parenthesizedTerm(PsiBuilder builder) {
        PsiBuilder.Marker parenthesized = builder.mark();
        ParserUtils.getToken(builder, TwelfTokenType.LPARENTH);
        if (!term(builder)) {
            parenthesized.rollbackTo();
            return false;
        }
        if (!ParserUtils.getToken(builder, TwelfTokenType.RPARENTH, TwelfBundle.message("expected.rParen"))) {
            builder.error(TwelfBundle.message("expected.rParen"));
            while (!builder.eof() && TwelfTokenType.DOT != builder.getTokenType() && TwelfTokenType.RPARENTH != builder.getTokenType()) {
                builder.error(TwelfBundle.message("expected.rParen"));      // todo why so verbose? already said once!
                builder.advanceLexer();
            }
            ParserUtils.getToken(builder, TwelfTokenType.RPARENTH);
        }
        parenthesized.done(TwelfElementType.PARENTHESIZED_EXPRESSION);
        return true;
    }

    private void declarationStatement(PsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        lfDeclaration(builder);
        dot(builder);
        statement.done(TwelfElementType.LF_DECLARATION_STATEMENT);
    }

    public boolean directive(PsiBuilder builder) {
        if (builder.getTokenType() != TwelfTokenType.DIRECTIVE) {
            return false;
        }

        PsiBuilder.Marker statement = builder.mark();
        String directive = builder.getTokenText();
        if (abbrevDirective(builder)) {
            statement.done(TwelfElementType.ABBREV_DIRECTIVE_STATEMENT);
        } else if (nameDirective(builder)) {
            statement.done(TwelfElementType.NAME_DIRECTIVE_STATEMENT);
        } else {
            if (TwelfTokenType.DIRECTIVE_SET.contains(directive)) {
                builder.advanceLexer();
            } else {
                PsiBuilder.Marker unknownDirective = builder.mark();
                builder.advanceLexer();
                unknownDirective.error(TwelfBundle.message("error.unknown.directive.0", directive));
            }
            dot(builder, true);
            statement.done(TwelfElementType.DIRECTIVE_STATEMENT);
        }

        return true;
    }

    private boolean abbrevDirective(PsiBuilder builder) {
        String directive = builder.getTokenText();
        if (!TwelfTokenType.D_ABBREV.equals(directive)) {
            return false;
        }
        builder.advanceLexer();

        if (!lfDeclaration(builder)) {
            builder.error(TwelfBundle.message("expected.declaration"));
        }

        dot(builder);
        return true;
    }

    private boolean nameDirective(PsiBuilder builder) {
        String directive = builder.getTokenText();
        if (!TwelfTokenType.D_NAME.equals(directive)) {
            return false;
        }
        builder.advanceLexer();

        if (idReference(builder)) {
            if (builder.getTokenType() == TwelfTokenType.IDENT) {
                PsiBuilder.Marker nameId = builder.mark();
                if (TwelfLexer.isUppercaseIdentifier(builder.getTokenText())) {
                    builder.advanceLexer();
                    nameId.done(TwelfElementType.IDENTIFIER);
                } else {
                    builder.advanceLexer();
                    nameId.error(TwelfBundle.message("expected.identifier.uppercase"));
                }
            } else {
                builder.error(TwelfBundle.message("expected.identifier.uppercase"));
            }
        } else {
            builder.error(TwelfBundle.message("expected.identifier"));
        }

        dot(builder);
        return true;
    }

    private void dot(PsiBuilder builder) {
        dot(builder, false);
    }

    private void dot(PsiBuilder builder, boolean ignoreUnexpected) {
        if (builder.getTokenType() != TwelfTokenType.DOT) {
            if (!ignoreUnexpected) {
                builder.error(TwelfBundle.message("expected.dot"));
            }
            while (!builder.eof() && builder.getTokenType() != TwelfTokenType.DOT) {
                builder.advanceLexer();
            }
        }
        builder.advanceLexer(); // eat the DOT
    }

}