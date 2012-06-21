package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.TwelfIdentifier;
import org.alahijani.lf.psi.api.TwelfIdentifierReference;
import org.alahijani.lf.psi.api.NameDirective;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class NameDirectiveImpl extends TwelfDirectiveImpl implements NameDirective {

    public NameDirectiveImpl(@NotNull ASTNode node) {
        super(node);
        assert getDirectiveName().equals(TwelfTokenType.D_NAME);
    }

//    @Override
//    public Icon getIcon(int flags) {
//        return TwelfIcons.NAME_DIRECTIVE;
//    }

    public TwelfIdentifierReference getForType() {
        return findChildByClass(TwelfIdentifierReference.class);
    }

    public TwelfIdentifier getNameIdentifier() {
        return findChildrenByClass(TwelfIdentifier.class)[0];
    }

    public TwelfIdentifier getSecondNameIdentifier() {
        TwelfIdentifier[] childrenByClass = findChildrenByClass(TwelfIdentifier.class);
        return childrenByClass.length > 1 ? childrenByClass[1] : null;
    }

}
