package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.TwelfDirective;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfDirectiveImpl extends TwelfStatementImpl implements TwelfDirective {

    private String directiveName;

    public TwelfDirectiveImpl(@NotNull ASTNode node) {
        this(node, findDirectiveName(node));
    }

    private static String findDirectiveName(ASTNode node) {
        for (ASTNode child : node.getChildren(null)) {
            if (child.getElementType() == TwelfTokenType.DIRECTIVE) {
                return child.getText();
            }
        }
        return null;
    }

    public TwelfDirectiveImpl(@NotNull ASTNode node, String directiveName) {
        super(node);
        this.directiveName = directiveName;
    }

    public String getDirectiveName() {
        return directiveName;
    }

}
