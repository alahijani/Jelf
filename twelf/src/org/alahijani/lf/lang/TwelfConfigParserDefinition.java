package org.alahijani.lf.lang;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.alahijani.lf.lexer.TwelfConfigLexer;
import org.alahijani.lf.parser.TwelfParser;
import org.alahijani.lf.parser.TwelfPsiCreator;
import org.alahijani.lf.psi.impl.TwelfConfigFileImpl;
import org.alahijani.lf.psi.stubs.elements.TwelfConfigStubFileElementType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigParserDefinition implements ParserDefinition {
    public static final IStubFileElementType TWELF_CONFIG_FILE = new TwelfConfigStubFileElementType();

    @NotNull
    public Lexer createLexer(Project project) {
        return new TwelfConfigLexer();
    }

    public PsiParser createParser(Project project) {
        return new TwelfParser();
    }

    public IFileElementType getFileNodeType() {
        return TWELF_CONFIG_FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TwelfConfigTokenType.WHITESPACE_BIT_SET;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return TwelfConfigTokenType.COMMENT_BIT_SET;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return TwelfPsiCreator.createTwelfConfigElement(node);
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new TwelfConfigFileImpl(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        if (left.getElementType() == TwelfConfigTokenType.END_OF_LINE_COMMENT) return SpaceRequirements.MUST_LINE_BREAK;

        Lexer lexer = new TwelfConfigLexer();
        return LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer);
    }

}
