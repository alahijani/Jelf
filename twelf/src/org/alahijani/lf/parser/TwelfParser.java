package org.alahijani.lf.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.TwelfTokenType;
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

        new TwelfParsing(builder).file();

        document.done(root);
        return builder.getTreeBuilt();

        // return debug(builder.getTreeBuilt(), "");
    }

    private ASTNode debug(ASTNode node, String indent) {
        IElementType type = node.getElementType();
        if (TwelfTokenType.WHITE_SPACE_OR_COMMENT_BIT_SET.contains(type)) {
            return node;
        }

        String s = indent + type;
        if (type == TwelfTokenType.IDENT || type == TwelfTokenType.KEYWORD || type == TwelfTokenType.DIRECTIVE) {
            s += " " + node.getText();
        }
        System.out.println(s);

        ASTNode[] children = node.getChildren(null);
        for (ASTNode child : children) {
            debug(child, "    " + indent);
        }

        return node;
    }

}