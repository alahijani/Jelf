package org.alahijani.lf.parser;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.TwelfElementType;
import org.alahijani.lf.TwelfTokenType;
import org.alahijani.lf.psi.impl.*;

/**
 * @author Ali Lahijani
 */
public class TwelfPsiCreator implements TwelfTokenType {
    public static PsiElement createElement(ASTNode node) {
        IElementType elem = node.getElementType();

        if (elem == TwelfElementType.LF_DECLARATION_STATEMENT) {
            return new LfDeclarationStatementImpl(node);
        }
        if (elem == TwelfElementType.ABBREV_DIRECTIVE_STATEMENT) {
            return new AbbrevDirectiveImpl(node);
        }
        if (elem == TwelfElementType.NAME_DIRECTIVE_STATEMENT) {
            return new NameDirectiveImpl(node);
        }
        if (elem == TwelfElementType.FIXITY_DIRECTIVE_STATEMENT) {
            return new TwelfDirectiveImpl(node);
        }
        if (elem == TwelfElementType.DIRECTIVE_STATEMENT) {
            return new TwelfDirectiveImpl(node);
        }
        if (elem == TwelfElementType.LF_DECLARATION) {
            return new LfDeclarationImpl(node);
        }
        if (elem == TwelfElementType.REFERENCE_EXPRESSION) {
            return new LfIdentifierReferenceImpl(node);
        }
        if (elem == TwelfElementType.PARENTHESIZED_EXPRESSION) {
            return new ParenthesizedExpressionImpl(node);
        }
        if (elem == TwelfElementType.APPLICATION) {
            return new PrefixApplicationExpressionImpl(node);
        }
        if (elem == TwelfElementType.POSTFIX_APPLICATION) {
            return new PostfixApplicationExpressionImpl(node);
        }
        if (elem == TwelfElementType.TYPED_TERM) {
            return new TypedExpressionImpl(node);
        }
        if (elem == TwelfElementType.TO_ARROW_TERM) {
            return new ToArrowTypeImpl(node);
        }
        if (elem == TwelfElementType.FROM_ARROW_TERM) {
            return new FromArrowTypeImpl(node);
        }
        if (elem == TwelfElementType.TYPE_KEYWORD) {
            return new TypeKeywordImpl(node);
        }
        if (elem == TwelfElementType.STRING_EXPRESSION) {
            return new StringExpressionImpl(node);
        }
        if (elem == TwelfElementType.PI_TERM) {
            return new PiTypeImpl(node);
        }
        if (elem == TwelfElementType.LAMBDA_TERM) {
            return new LambdaExpressionImpl(node);
        }
        if (elem == TwelfElementType.IDENTIFIER) {
            return new LfIdentifierImpl(node);
        }

        return new ASTWrapperPsiElement(node);

    }
}
