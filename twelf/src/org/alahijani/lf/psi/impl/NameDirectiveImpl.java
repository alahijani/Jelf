package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.TwelfTokenType;
import org.alahijani.lf.psi.api.LfIdentifier;
import org.alahijani.lf.psi.api.LfIdentifierReference;
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

    public LfIdentifierReference getForType() {
        return findChildByClass(LfIdentifierReference.class);
    }

    public LfIdentifier getNameIdentifier() {
        return findChildByClass(LfIdentifier.class);
    }

}
