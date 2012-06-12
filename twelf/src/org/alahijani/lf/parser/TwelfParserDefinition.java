package org.alahijani.lf.parser;

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
import org.alahijani.lf.Twelf;
import org.alahijani.lf.TwelfTokenType;
import org.alahijani.lf.lexer.TwelfLexer;
import org.alahijani.lf.psi.impl.TwelfFileImpl;
import org.alahijani.lf.psi.stubs.elements.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfParserDefinition implements ParserDefinition {
    public static final IStubFileElementType TWELF_FILE = new TwelfStubFileElementType(Twelf.INSTANCE);

    @NotNull
    public Lexer createLexer(Project project) {
        return new TwelfLexer();
    }

    public PsiParser createParser(Project project) {
        return new TwelfParser();
    }

    public IFileElementType getFileNodeType() {
        return TWELF_FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TwelfTokenType.WHITESPACE_BIT_SET;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return TwelfTokenType.COMMENT_BIT_SET;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TwelfTokenType.STRING_LITERALS;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return TwelfPsiCreator.createElement(node);
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new TwelfFileImpl(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        if (left.getElementType() == TwelfTokenType.END_OF_LINE_COMMENT) return SpaceRequirements.MUST_LINE_BREAK;

        Lexer lexer = new TwelfLexer();
        final SpaceRequirements spaceRequirements = LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer);

        return spaceRequirements;
    }

}
