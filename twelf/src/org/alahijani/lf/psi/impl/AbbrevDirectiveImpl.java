package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.TwelfTokenType;
import org.alahijani.lf.psi.api.GlobalVariableBinder;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.light.LightLfDeclaration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ali Lahijani
 */
public class AbbrevDirectiveImpl extends TwelfDirectiveImpl implements GlobalVariableBinder {

    private List<LfDeclaration> metaVariables = new ArrayList<LfDeclaration>();

    public AbbrevDirectiveImpl(@NotNull ASTNode node) {
        super(node);
        assert getDirectiveName().equals(TwelfTokenType.D_ABBREV);
    }


    public LfDeclaration declareMeta(String name) {
        LfDeclaration declaration = LightLfDeclaration.declareMeta(this, name);
        // add(declaration);
        metaVariables.add(declaration);
        return declaration;
    }

    public LfDeclaration[] getMetaVariables() {
        return metaVariables.toArray(new LfDeclaration[metaVariables.size()]);
    }

    public LfDeclaration getDeclaration() {
        return findChildByClass(LfDeclaration.class);
    }

    @Override
    public Icon getIcon(int flags) {
        return TwelfIcons.ABBREV_DIRECTIVE;
    }
}
