package org.alahijani.lf.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.lang.TwelfElementType;
import org.alahijani.lf.lexer.TwelfLexer;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.alahijani.lf.lang.TwelfTokenType.*;

/**
 * @author Ali Lahijani
 */
public class TwelfParsing {
    private PsiBuilder builder;

    private static final IElementType USER_DEFINED_OPERATOR = null;

    private Map<String, Operator> operatorMap = new LinkedHashMap<String, Operator>(); {
        operatorMap.put("", Operator.juxOp);
        operatorMap.put("->", Operator.arrowOp);
        operatorMap.put("<-", Operator.backArrowOp);
        operatorMap.put(":", Operator.colonOp);
    }

    @Nullable
    private Operator getOperator() {
        String text = builder.getTokenText();
        Operator operator = operatorMap.get(text);

        if (operator == null && !builder.eof()) {
            if (APPLICATION_CANDIDATE.contains(builder.getTokenType())) {
                operator = Operator.juxOp;
            }
        }

        return operator;
    }

    public TwelfParsing(PsiBuilder builder) {
        this.builder = builder;
    }

    public boolean document() {
        while (!builder.eof()) {
            statement();
        }
        return true;
    }

    private boolean statement() {

        IElementType type = builder.getTokenType();
        if (type == IDENT) {
            declarationStatement();
        } else if (type == DIRECTIVE) {
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
        eatElement(TwelfElementType.IDENTIFIER);

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

        declaration.done(TwelfElementType.LF_GLOBAL_VARIABLE);
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

        Operator op = getOperator();
        if (op == null || op.precedence < minPrecedence) {
            lhs.drop();
            return lhs;      // todo: just has been dropped!
        }

        while (true) {

            if (op.elementType == USER_DEFINED_OPERATOR) {
                String id = idReference();
                assert id != null;
                lhs.done(TwelfElementType.POSTFIX_APPLICATION);
                lhs = lhs.precede();
            } else if (op != Operator.juxOp) {
                builder.advanceLexer();
            }

            PsiBuilder.Marker rhs = builder.mark();
            if (!primaryTerm()) {
                builder.error(TwelfBundle.message("expected.term"));
                rhs.drop();
                done(lhs, op);
                return lhs;
            }

            termR(rhs, op.precedence);

            done(lhs, op);

            op = getOperator();
            if (op == null || op.precedence < minPrecedence) {
                return lhs;
            }

            lhs = lhs.precede();
        }

    }

    private void termR(PsiBuilder.Marker rhs, int minPrecedence) {
        /**
         * while the next token is
         *      a binary operator whose precedence is greater than op's, or
         *      a right-associative operator whose precedence is equal to op's
         */
        while (true) {
            Operator op = getOperator();
            if (!(op instanceof Operator.Infix)) {
                rhs.drop();
                break; // todo
            }

            if (op.precedence < minPrecedence) {
                rhs.drop();
                break;
            }
            if (op.precedence == minPrecedence) {
                if (((Operator.Infix) op).associativity != Operator.Associativity.Right) {
                    rhs.drop();
                    break;
                }
            }

            rhs = termL(rhs, op.precedence);
            rhs = rhs.precede();
        }
    }

    private void done(PsiBuilder.Marker lhs, Operator op) {
        IElementType type = op.elementType;
        if (type == USER_DEFINED_OPERATOR) {
            type = TwelfElementType.APPLICATION;
        }
        lhs.done(type);
    }

    private boolean primaryTerm() {

        if (KEYWORD == builder.getTokenType() && KEYWORD_TYPE.equals(builder.getTokenText())) {
            eatElement(TwelfElementType.TYPE_KEYWORD);
            return true;
        }

        if (builder.getTokenType() == IDENT) {
            return idReference() != null;
        }

        if (builder.getTokenType() == STRING_LITERAL) {
            eatElement(TwelfElementType.STRING_EXPRESSION);
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

    private String idReference() {
        if (builder.getTokenType() != IDENT) {
            return null;
        }

        String text = builder.getTokenText();

        PsiBuilder.Marker idReference = builder.mark();
        eatElement(TwelfElementType.IDENTIFIER);
        idReference.done(TwelfElementType.REFERENCE_EXPRESSION);

        return text;
    }

    private void localDeclaration(IElementType rBracket, String rBracketExpected) {
        builder.advanceLexer();     // eat the left bracket

        if (builder.getTokenType() == IDENT) {
            PsiBuilder.Marker declaration = builder.mark();
            eatElement(TwelfElementType.IDENTIFIER);
            if (builder.getTokenType() == COLON) {
                builder.advanceLexer();
                if (!term()) {
                    builder.error(TwelfBundle.message("expected.type"));
                }
            }
            declaration.done(TwelfElementType.LF_LOCAL_VARIABLE);
        } else {
            builder.error(TwelfBundle.message("expected.identifier"));
        }

        rightBracket(rBracket, rBracketExpected);
    }

    private void rightBracket(IElementType rBracket, String rBracketExpected) {
        while (true) {
            IElementType type = builder.getTokenType();

            if (type == rBracket) {
                builder.advanceLexer();
                break;
            }

            if (type == DOT || builder.eof()) {
                builder.error(rBracketExpected);
                break;
            }

            builder.advanceLexer();
        }
    }

    private boolean binderTerm(IElementType lBracket,
                               IElementType rBracket,
                               IElementType elementType,
                               String rBracketExpected) {
        PsiBuilder.Marker bracketTerm = builder.mark();

        localDeclaration(rBracket, rBracketExpected);

        if (!term()) {
            builder.error(TwelfBundle.message("expected.term"));
        }

        bracketTerm.done(elementType);

        return true;
    }

    private boolean parenthesizedTerm() {
        PsiBuilder.Marker parenthesized = builder.mark();
        builder.advanceLexer();     // eat the left parenthesis

        if (!term()) {
            builder.error(TwelfBundle.message("expected.term"));
        }

        rightBracket(RPARENTH, TwelfBundle.message("expected.rParen"));
        parenthesized.done(TwelfElementType.PARENTHESIZED_EXPRESSION);

        return true;
    }

    private void declarationStatement() {
        PsiBuilder.Marker statement = builder.mark();
        lfDeclaration();
        dot();
        statement.done(TwelfElementType.LF_DECLARATION_STATEMENT);
    }

    private boolean directive() {
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
        } else if (modedDirective()) {
            statement.done(TwelfElementType.MODED_DIRECTIVE_STATEMENT);
        } else if (terminationDirective()) {
            statement.done(TwelfElementType.TERMINATION_DIRECTIVE_STATEMENT);
        } else if (worldsDirective()) {
            statement.done(TwelfElementType.WORLDS_DIRECTIVE_STATEMENT);
        } else {
            if (DIRECTIVE_SET.contains(directive)) {
                builder.advanceLexer();
            } else {
                PsiBuilder.Marker unknownDirective = builder.mark();
                builder.advanceLexer();
                unknownDirective.error(TwelfBundle.message("error.unknown.directive.0", directive));
            }
            dot(false);
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

        if (idReference() != null) {
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

            if (builder.getTokenType() == IDENT) {      // optional second identifier
                eatElement(TwelfElementType.IDENTIFIER);
            }
        } else {
            builder.error(TwelfBundle.message("expected.identifier"));
        }

        dot();
        return true;
    }

    private boolean modedDirective() {
        String directive = builder.getTokenText();
        if (!D_MODE.equals(directive)
                && !D_UNIQUE.equals(directive)
                && !D_COVERS.equals(directive)) {
            return false;
        }
        builder.advanceLexer();

        modeDeclaration();

        dot();
        return true;
    }

    // TODO support parenthesized mode declarations
    private void modeDeclaration() {
        if (!fullModeDeclaration()) {
            shortModeDeclaration();
        }
    }

    private boolean fullModeDeclaration() {
        /**
         * Whether a Full Mode Declaration or a Short Mode Declaration
         */
        boolean fullMode = false;
        while (true) {
            if (builder.getTokenType() != IDENT) break;

            String text = builder.getTokenText();
            if (text == null || text.length() != 1) break;

            char c = text.charAt(0);
            if (c != '-' && c != '+' && c != '*') break;

            PsiBuilder.Marker modedLocalDeclaration = builder.mark();
            eatElement(TwelfElementType.POSITIONAL_KEYWORD);

            if (builder.getTokenType() == LBRACE) {
                localDeclaration(RBRACE, TwelfBundle.message("expected.rBrace"));
                modedLocalDeclaration.done(TwelfElementType.MODED_LOCAL_DECLARATION);
                fullMode = true;
            } else {
                modedLocalDeclaration.rollbackTo();
                break;
            }
        }

        if (fullMode) {
            if (!term()) {
                builder.error(TwelfBundle.message("expected.term"));
            }
        }
        return fullMode;
    }

    private void shortModeDeclaration() {
        if (idReference() != null) {            // the head
            while (true) {
                if (builder.getTokenType() != IDENT) {
                    break;
                }

                PsiBuilder.Marker modedIdentifier = builder.mark();

                String var = builder.getTokenText();
                if (var == null || var.length() < 2) {
                    modedIdentifier.error(TwelfBundle.message("expected.identifier.moded"));
                    break;
                }

                char c = var.charAt(0);
                if (c != '-' && c != '+' && c != '*') {
                    modedIdentifier.error(TwelfBundle.message("expected.identifier.moded"));
                    break;
                }

                builder.advanceLexer();
                modedIdentifier.done(TwelfElementType.IDENTIFIER);
            }
        } else {
            builder.error(TwelfBundle.message("expected.identifier"));
        }
    }

    private boolean worldsDirective() {
        String directive = builder.getTokenText();
        if (!D_WORLDS.equals(directive)) {
            return false;
        }
        builder.advanceLexer();

        labelList();
        callPatterns();

        dot();
        return true;
    }

    private boolean terminationDirective() {
        String directive = builder.getTokenText();
        if (!D_TERMINATES.equals(directive)
                &&  !D_TOTAL.equals(directive)) {
            return false;
        }
        builder.advanceLexer();

        terminationOrder();
        callPatterns();

        dot();
        return true;
    }

    private void callPatterns() {
        while (builder.getTokenType() == LPARENTH) {
            builder.advanceLexer();

            PsiBuilder.Marker callPattern = builder.mark();
            if (idReference() != null) {            // the head
                while (builder.getTokenType() == IDENT) {
                    eatElement(TwelfElementType.IDENTIFIER);
                }
            } else {
                builder.error(TwelfBundle.message("expected.identifier"));
            }
            callPattern.done(TwelfElementType.CALL_PATTERN);

            rightBracket(RPARENTH, TwelfBundle.message("expected.rParen"));
        }
    }

    private void labelList() {
        if (builder.getTokenType() == LPARENTH) {
            builder.advanceLexer();
            while (true) {
                if (builder.getTokenType() == IDENT) {
                    PsiBuilder.Marker labelReference = builder.mark();
                    eatElement(TwelfElementType.IDENTIFIER);
                    labelReference.done(TwelfElementType.BLOCK_LABEL_REFERENCE);
                }
                // TODO buggy
                if (builder.getTokenType() == IDENT && "|".equals(builder.getTokenText())) {
                    eatElement(TwelfElementType.POSITIONAL_KEYWORD);
                } else if (builder.getTokenType() != IDENT) {
                    break;
                }
            }
            rightBracket(RPARENTH, TwelfBundle.message("expected.rParen"));
        } else {
            builder.error(TwelfBundle.message("expected.label.list"));
        }
    }

    private void terminationOrder() {
        PsiBuilder.Marker terminationOrder = builder.mark();
        IElementType tokenType = builder.getTokenType();

        if (tokenType == IDENT) {               // single argument
            eatElement(TwelfElementType.IDENTIFIER);
        } else if (tokenType == LPARENTH) {     // mutual arguments
            builder.advanceLexer();
            while (builder.getTokenType() == IDENT) {
                eatElement(TwelfElementType.IDENTIFIER);
            }
            rightBracket(RPARENTH, TwelfBundle.message("expected.rParen"));
        } else if (tokenType == LBRACE) {       // lexicographic order
            builder.advanceLexer();
            while (true) {
                IElementType type = builder.getTokenType();
                if (type == RBRACE) {
                    builder.advanceLexer();
                    break;
                }

                if (type == DOT || builder.eof()) {
                    builder.error(TwelfBundle.message("expected.rBrace"));
                    break;
                }

                terminationOrder();
            }
        } else if (tokenType == LBRACKET) {     // simultaneous order
            builder.advanceLexer();
            while (true) {
                IElementType type = builder.getTokenType();
                if (type == RBRACKET) {
                    builder.advanceLexer();
                    break;
                }

                if (type == DOT || builder.eof()) {
                    builder.error(TwelfBundle.message("expected.rBracket"));
                    break;
                }

                terminationOrder();
            }
        } else {
            builder.advanceLexer();
            terminationOrder.error(TwelfBundle.message("expected.termination.order"));
            return;
        }

        terminationOrder.done(TwelfElementType.TERMINATION_ORDER);
    }


    private boolean fixityDirective() {
        String directive = builder.getTokenText();

        Operator.Associativity associativity = null;
        if (D_PREFIX.equals(directive) || D_POSTFIX.equals(directive)) {
            builder.advanceLexer();
        } else if (D_INFIX.equals(directive)) {
            builder.advanceLexer();
            associativity = associativity();
        } else {
            return false;
        }

        Integer precedence = precedence();
        String id = idReference();
        if (id != null) {
            if (precedence != null && associativity != null) {
                operatorMap.put(id, new Operator.Infix(precedence, associativity, USER_DEFINED_OPERATOR));
            }
        } else {
            builder.error(TwelfBundle.message("expected.identifier"));
        }
        // todo implement postfix and prefix...

        dot();
        return true;
    }

    private Operator.Associativity associativity() {
        if (builder.getTokenType() != IDENT) {
            builder.error(TwelfBundle.message("expected.associativity"));
            return null;
        }

        PsiBuilder.Marker associativity = builder.mark();
        String text = builder.getTokenText();
        builder.advanceLexer();
        if ("left".equals(text)) {
            associativity.done(TwelfElementType.ASSOCIATIVITY);
            return Operator.Associativity.Left;
        } else if ("right".equals(text)) {
            associativity.done(TwelfElementType.ASSOCIATIVITY);
            return Operator.Associativity.Right;
        } else if ("none".equals(text)) {                           // todo implement none
            associativity.done(TwelfElementType.ASSOCIATIVITY);
            return null;
        } else {
            associativity.error(TwelfBundle.message("expected.associativity"));
            return null;
        }
    }

    private Integer precedence() {
        if (builder.getTokenType() != IDENT) {
            builder.error(TwelfBundle.message("expected.integer"));
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
        dot(true);
    }

    private void dot(boolean reportUnexpected) {
        do {
            if (builder.getTokenType() == DOT) {
                builder.advanceLexer();
                return;
            }
            if (builder.eof()) {
                builder.error(TwelfBundle.message("expected.dot"));
                return;
            }

            // advance the lexer and issue error if required
            if (reportUnexpected) {
                reportUnexpected();
                reportUnexpected = false;    // just one error is enough
            } else {
                builder.advanceLexer();
            }
        } while (true);
    }

    private void reportUnexpected() {
        PsiBuilder.Marker marker = builder.mark();
        String unexpected = builder.getTokenText();
        builder.advanceLexer();
        marker.error(TwelfBundle.message("unexpected.token", unexpected));
    }

    private void eatElement(IElementType elementType) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        marker.done(elementType);
    }

}

// Jelf means extravagant or immodest