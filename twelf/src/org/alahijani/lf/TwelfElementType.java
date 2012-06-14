package org.alahijani.lf;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.psi.stubs.elements.LfDeclarationElementType;
import org.alahijani.lf.psi.tree.ITwelfElementType;


public interface TwelfElementType extends TokenType {

    IElementType LF_DECLARATION_STATEMENT = new ITwelfElementType("Lf declaration statement");

    IElementType DIRECTIVE_STATEMENT = new ITwelfElementType("Twelf directive (other)");
    IElementType ABBREV_DIRECTIVE_STATEMENT = new ITwelfElementType("%abbrev directive");
    IElementType NAME_DIRECTIVE_STATEMENT = new ITwelfElementType("%name directive");
    IElementType FIXITY_DIRECTIVE_STATEMENT = new ITwelfElementType("Operator fixity directive");
    IElementType ASSOCIATIVITY = new ITwelfElementType("Associativity");
    IElementType PRECEDENCE = new ITwelfElementType("Precedence");

    IElementType PARENTHESIZED_EXPRESSION = new ITwelfElementType("Parenthesized expression");
    IElementType APPLICATION = new ITwelfElementType("Application");
    IElementType POSTFIX_APPLICATION = new ITwelfElementType("Postfix application");
    IElementType TYPED_TERM = new ITwelfElementType("Typed term");
    IElementType TO_ARROW_TERM = new ITwelfElementType("-> type");
    IElementType FROM_ARROW_TERM = new ITwelfElementType("<- type");
    IElementType LAMBDA_TERM = new ITwelfElementType("Lambda term");
    IElementType PI_TERM = new ITwelfElementType("Pi term");
    IElementType IDENTIFIER = new ITwelfElementType("Identifier");

    IElementType TYPE_KEYWORD = new ITwelfElementType("'type' keyword");
    IElementType REFERENCE_EXPRESSION = new ITwelfElementType("Reference expressions");
    IElementType STRING_EXPRESSION = new ITwelfElementType("String literal expression");

    IElementType LF_DECLARATION = new LfDeclarationElementType("Lf declaration");
}
