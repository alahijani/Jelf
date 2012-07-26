package org.alahijani.lf.lang;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.psi.stubs.elements.LfGlobalVariableElementType;
import org.alahijani.lf.psi.tree.ITwelfElementType;


public interface TwelfElementType extends TokenType {

    IElementType TWELF_FILE = TwelfParserDefinition.TWELF_FILE;

    IElementType LF_DECLARATION_STATEMENT = new ITwelfElementType("Lf declaration statement");

    IElementType DIRECTIVE_STATEMENT = new ITwelfElementType("Twelf directive (other)");
    IElementType ABBREV_DIRECTIVE_STATEMENT = new ITwelfElementType("%abbrev directive");
    IElementType NAME_DIRECTIVE_STATEMENT = new ITwelfElementType("%name directive");
    IElementType FIXITY_DIRECTIVE_STATEMENT = new ITwelfElementType("Operator fixity directive");
    IElementType MODED_DIRECTIVE_STATEMENT = new ITwelfElementType("%mode, %covers or %unique directive");
    IElementType TERMINATION_DIRECTIVE_STATEMENT = new ITwelfElementType("%terminates or %total directive");
    IElementType WORLDS_DIRECTIVE_STATEMENT = new ITwelfElementType("%worlds directive");
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
    IElementType POSITIONAL_KEYWORD = new ITwelfElementType("Positional keyword");          // ?
    IElementType MODED_LOCAL_DECLARATION = new ITwelfElementType("Moded local declaration");
    IElementType TERMINATION_ORDER = new ITwelfElementType("Termination order");
    IElementType CALL_PATTERN = new ITwelfElementType("Call pattern");
    IElementType BLOCK_LABEL_REFERENCE = new ITwelfElementType("Block label reference");

    IElementType TYPE_KEYWORD = new ITwelfElementType("'type' keyword");
    IElementType REFERENCE_EXPRESSION = new ITwelfElementType("Reference expressions");
    IElementType STRING_EXPRESSION = new ITwelfElementType("String literal expression");

    IElementType LF_GLOBAL_VARIABLE = new LfGlobalVariableElementType("Lf global variable");
    IElementType LF_LOCAL_VARIABLE = new ITwelfElementType("Lf local variable");
    IElementType LF_META_VARIABLE = new ITwelfElementType("Lf meta-variable");

    IElementType TWELF_CONFIG_FILE = TwelfConfigParserDefinition.TWELF_CONFIG_FILE;
    IElementType TWELF_FILE_REFERENCE = new IElementType("Twelf file reference", TwelfConfig.INSTANCE);
    IElementType FILE_NAME = new IElementType("Twelf file name", TwelfConfig.INSTANCE);
}
