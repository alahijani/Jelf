package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.impl.source.tree.Factory;
import com.intellij.psi.impl.source.tree.SharedImplUtil;
import com.intellij.util.CharTable;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.lexer.TwelfLexer;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.LfIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LfIdentifierImpl extends TwelfElementImpl implements LfIdentifier {
    public LfIdentifierImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void setText(String text) {
        ASTNode node = this.getNode();
        CharTable charTableByTree = SharedImplUtil.findCharTableByTree(node);

        ASTNode oldChild = node.getFirstChildNode();
        ASTNode newChild = Factory.createSingleLeafElement(TwelfTokenType.IDENT, text, charTableByTree, this.getManager());
        node.replaceChild(oldChild, newChild);
    }

    public boolean isUppercase() {
        return TwelfLexer.isUppercaseIdentifier(getText());
    }

    public boolean isAnonymous() {
        return TwelfLexer.isAnonymousIdentifier(getText());
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TwelfElementVisitor) {
            ((TwelfElementVisitor) visitor).visitIdentifier(this);
        } else {
            visitor.visitElement(this);
        }
    }
}
