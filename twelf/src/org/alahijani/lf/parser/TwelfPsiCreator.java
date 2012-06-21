package org.alahijani.lf.parser;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.lang.TwelfElementType;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.impl.*;

/**
 * @author Ali Lahijani
 */
public class TwelfPsiCreator implements TwelfTokenType {
    public static PsiElement createTwelfElement(ASTNode node) {
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
        if (elem == TwelfElementType.LF_GLOBAL_VARIABLE) {
            return new LfGlobalVariableImpl(node);
        }
        if (elem == TwelfElementType.LF_LOCAL_VARIABLE) {
            return new LfLocalVariableImpl(node);
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
            return new TwelfIdentifierImpl(node);
        }

        return new ASTWrapperPsiElement(node);

    }

    public static PsiElement createTwelfConfigElement(ASTNode node) {
        IElementType elem = node.getElementType();

        if (elem == TwelfElementType.FILE_NAME) {
            return new TwelfIdentifierImpl(node);
        }
        if (elem == TwelfElementType.TWELF_FILE_REFERENCE) {
            return new TwelfFileReferenceImpl(node);
        }

        return new ASTWrapperPsiElement(node);
    }

}
