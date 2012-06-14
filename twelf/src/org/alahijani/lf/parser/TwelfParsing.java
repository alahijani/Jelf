package org.alahijani.lf.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.TwelfElementType;
import org.alahijani.lf.lexer.TwelfLexer;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.alahijani.lf.TwelfTokenType.*;

/**
 * @author Ali Lahijani
 */
public class TwelfParsing {
    private PsiBuilder builder;

    private static final IElementType USER_DEFINED_OPERATOR = null;

    private Map<String, Operation.Operator> operatorMap = new LinkedHashMap<String, Operation.Operator>(); {
        operatorMap.put("", Operation.juxOp);
        operatorMap.put("->", Operation.arrowOp);
        operatorMap.put("<-", Operation.backArrowOp);
        operatorMap.put(":", Operation.colonOp);
    }

    @Nullable
    private Operation.Operator getOperator() {
        String text = builder.getTokenText();
        Operation.Operator operator = operatorMap.get(text);

        if (operator == null && !builder.eof()) {
            IElementType tokenType = builder.getTokenType();
            if (tokenType != DOT &&
                    tokenType != RPARENTH &&
                    tokenType != RBRACE &&
                    tokenType != RBRACKET &&
                    !(tokenType == KEYWORD && KEYWORD_EQ.equals(text))) {
                operator = Operation.juxOp;
            }
        }

        return operator;
    }

    public TwelfParsing(PsiBuilder builder) {
        this.builder = builder;
    }

    public boolean file() {
        while (!builder.eof()) {
            statement();
        }
        return true;
    }

    public boolean statement() {

        if (builder.getTokenType() == IDENT) {
            declarationStatement();
        } else if (builder.getTokenType() == DIRECTIVE) {
            directive();
        } else {
            builder.error(TwelfBundle.message("expected.statement"));
            dot();
        }

        return true;
    }

    private boolean lfDeclaration() {
        if (builder.getTokenType() != IDENT) {
            return false;
        }

        PsiBuilder.Marker declaration = builder.mark();
        ParserUtils.eatElement(builder, TwelfElementType.IDENTIFIER);

        boolean colonOrEq = false;
        if (builder.getTokenType() == COLON) {
            builder.advanceLexer();
            if (!term()) {
                builder.error(TwelfBundle.message("expected.term"));
            }
            colonOrEq = true;
        }

        if (KEYWORD == builder.getTokenType() && KEYWORD_EQ.equals(builder.getTokenText())) {
            builder.advanceLexer();
            if (!term()) {
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

    private boolean term() {
        PsiBuilder.Marker lhs = builder.mark();
        if (!primaryTerm()) {
            lhs.drop();  // not needed for a single primary term
            return false;
        }

        termL(lhs, 0);
        return true;
    }


    private PsiBuilder.Marker termL(PsiBuilder.Marker lhs, int minPrecedence) {

        Operation.Operator op = getOperator();
        if (op == null || op.getPrecedence() < minPrecedence) {
            lhs.drop();
            return lhs;      // todo: just have been dropped!
        }

        while (true) {
            if (op != Operation.juxOp) {
                builder.advanceLexer();
            }

            if (op.getElementType() == USER_DEFINED_OPERATOR) {
                lhs.done(TwelfElementType.POSTFIX_APPLICATION);
                lhs = lhs.precede();
            }

            PsiBuilder.Marker rhs = builder.mark();
            if (!primaryTerm()) {
                builder.error(TwelfBundle.message("expected.term"));
                rhs.drop();
                done(lhs, op);
                return lhs;
            }

            termR(rhs, op.getPrecedence());

            done(lhs, op);

            op = getOperator();
            if (op == null || op.getPrecedence() < minPrecedence) {
                return lhs;
            }

            lhs = lhs.precede();
        }

    }

    private void done(PsiBuilder.Marker lhs, Operation.Operator op) {
        IElementType type = op.getElementType();
        if (type == USER_DEFINED_OPERATOR) {
            type = TwelfElementType.APPLICATION;
        }
        lhs.done(type);
    }

    private void termR(PsiBuilder.Marker rhs, int minPrecedence) {
        /**
         * while the next token is
         *      a binary operator whose precedence is greater than op's, or
         *      a right-associative operator whose precedence is equal to op's
         */
        while (true) {
            Operation.Operator lookahead = getOperator();
            if (!(lookahead instanceof Operation.Infix)) {
                rhs.drop();
                break; // todo
            }

            if (lookahead.getPrecedence() < minPrecedence) {
                rhs.drop();
                break;
            }
            if (lookahead.getPrecedence() == minPrecedence) {
                if (((Operation.Infix) lookahead).getAssociativity() != Operation.Associativity.Right) {
                    rhs.drop();
                    break;
                }
            }

            rhs = termL(rhs, lookahead.getPrecedence());
            rhs = rhs.precede();
        }
    }

    private boolean primaryTerm() {

        if (KEYWORD == builder.getTokenType() && KEYWORD_TYPE.equals(builder.getTokenText())) {
            ParserUtils.eatElement(builder, TwelfElementType.TYPE_KEYWORD);
            return true;
        }

        if (builder.getTokenType() == IDENT) {
            return idReference();
        }

        if (builder.getTokenType() == STRING_LITERAL) {
            ParserUtils.eatElement(builder, TwelfElementType.STRING_EXPRESSION);
            return true;
        }

        if (builder.getTokenType() == LPARENTH) {
            return parenthesizedTerm();
        }

        if (builder.getTokenType() == LBRACKET) {
            return binderTerm(LBRACKET, RBRACKET, TwelfElementType.LAMBDA_TERM, TwelfBundle.message("expected.rBracket"));
        }

        if (builder.getTokenType() == LBRACE) {
            return binderTerm(LBRACE, RBRACE, TwelfElementType.PI_TERM, TwelfBundle.message("expected.rBrace"));
        }

        return false;
    }

    private boolean idReference() {
        if (builder.getTokenType() != IDENT) {
            return false;
        }

        PsiBuilder.Marker idReference = builder.mark();
        ParserUtils.eatElement(builder, TwelfElementType.IDENTIFIER);
        idReference.done(TwelfElementType.REFERENCE_EXPRESSION);
        return true;
    }

    private boolean binderTerm(IElementType lBracket,
                               IElementType rBracket,
                               IElementType elementType,
                               String rBracketExpected) {
        PsiBuilder.Marker bracketTerm = builder.mark();
        builder.advanceLexer();

        PsiBuilder.Marker paramDecl = builder.mark();
        if (builder.getTokenType() == IDENT) {
            ParserUtils.eatElement(builder, TwelfElementType.IDENTIFIER);
            if (builder.getTokenType() == COLON) {
                builder.advanceLexer();
                if (!term()) {
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

        if (!term()) {
            builder.error(TwelfBundle.message("expected.term"));
        }

        bracketTerm.done(elementType);
        return true;
    }

    public boolean parenthesizedTerm() {
        PsiBuilder.Marker parenthesized = builder.mark();
        ParserUtils.getToken(builder, LPARENTH);
        if (!term()) {
            parenthesized.rollbackTo();
            return false;
        }
        if (!ParserUtils.getToken(builder, RPARENTH, TwelfBundle.message("expected.rParen"))) {
            builder.error(TwelfBundle.message("expected.rParen"));
            while (!builder.eof() && DOT != builder.getTokenType() && RPARENTH != builder.getTokenType()) {
                builder.error(TwelfBundle.message("expected.rParen"));      // todo why so verbose? already said once!
                builder.advanceLexer();
            }
            ParserUtils.getToken(builder, RPARENTH);
        }
        parenthesized.done(TwelfElementType.PARENTHESIZED_EXPRESSION);
        return true;
    }

    private void declarationStatement() {
        PsiBuilder.Marker statement = builder.mark();
        lfDeclaration();
        dot();
        statement.done(TwelfElementType.LF_DECLARATION_STATEMENT);
    }

    public boolean directive() {
        if (builder.getTokenType() != DIRECTIVE) {
            return false;
        }

        PsiBuilder.Marker statement = builder.mark();
        String directive = builder.getTokenText();
        if (abbrevDirective()) {
            statement.done(TwelfElementType.ABBREV_DIRECTIVE_STATEMENT);
        } else if (nameDirective()) {
            statement.done(TwelfElementType.NAME_DIRECTIVE_STATEMENT);
        } else if (fixityDirective()) {
            statement.done(TwelfElementType.FIXITY_DIRECTIVE_STATEMENT);
        } else {
            if (DIRECTIVE_SET.contains(directive)) {
                builder.advanceLexer();
            } else {
                PsiBuilder.Marker unknownDirective = builder.mark();
                builder.advanceLexer();
                unknownDirective.error(TwelfBundle.message("error.unknown.directive.0", directive));
            }
            dot(true);
            statement.done(TwelfElementType.DIRECTIVE_STATEMENT);
        }

        return true;
    }

    private boolean abbrevDirective() {
        String directive = builder.getTokenText();
        if (!D_ABBREV.equals(directive)) {
            return false;
        }
        builder.advanceLexer();

        if (!lfDeclaration()) {
            builder.error(TwelfBundle.message("expected.declaration"));
        }

        dot();
        return true;
    }

    private boolean nameDirective() {
        String directive = builder.getTokenText();
        if (!D_NAME.equals(directive)) {
            return false;
        }
        builder.advanceLexer();

        if (idReference()) {
            if (builder.getTokenType() == IDENT) {
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

        dot();
        return true;
    }

    private boolean fixityDirective() {
        String directive = builder.getTokenText();
        if (D_PREFIX.equals(directive) || D_POSTFIX.equals(directive)) {
            builder.advanceLexer();
        } else if (D_INFIX.equals(directive)) {
            builder.advanceLexer();
            Operation.Associativity associativity = associativity();
        } else {
            return false;
        }

        Integer precedence = precedence();
        if (idReference()) {
            if (precedence != null) {
                new Operation.Operator(precedence, USER_DEFINED_OPERATOR);
            }
        } else {
            builder.error(TwelfBundle.message("expected.identifier"));
        }

        dot();
        return true;
    }

    private Operation.Associativity associativity() {
        if (builder.getTokenType() != IDENT) {
            builder.error(TwelfBundle.message("expected.left.or.right"));
            return null;
        }

        PsiBuilder.Marker associativity = builder.mark();
        String text = builder.getTokenText();
        builder.advanceLexer();
        if ("left".equals(text)) {
            associativity.done(TwelfElementType.ASSOCIATIVITY);
            return Operation.Associativity.Left;
        } else if ("right".equals(text)) {
            associativity.done(TwelfElementType.ASSOCIATIVITY);
            return Operation.Associativity.Right;
        } else {
            associativity.error(TwelfBundle.message("expected.left.or.right"));
            return null;
        }
    }

    private Integer precedence() {
        if (builder.getTokenType() != IDENT) {
            builder.error(TwelfBundle.message("expected.left.or.right"));
            return null;
        }

        PsiBuilder.Marker precedence = builder.mark();
        String text = builder.getTokenText();
        builder.advanceLexer();
        try {
            int i = Integer.parseInt(text);
            precedence.done(TwelfElementType.PRECEDENCE);
            // todo max, min
            return i;
        } catch (NumberFormatException e) {
            precedence.error(TwelfBundle.message("expected.integer"));
            return null;
        }
    }

    private void dot() {
        dot(false);
    }

    private void dot(boolean ignoreUnexpected) {
        if (builder.getTokenType() != DOT) {
            if (!ignoreUnexpected) {
                builder.error(TwelfBundle.message("expected.dot"));
            }
            while (!builder.eof() && builder.getTokenType() != DOT) {
                builder.advanceLexer();
            }
        }
        builder.advanceLexer(); // eat the DOT
    }

}
