package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.LfDeclarationStatement;
import org.alahijani.lf.psi.light.LightLfDeclaration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ali Lahijani
 */
public class LfDeclarationStatementImpl extends TwelfStatementImpl implements LfDeclarationStatement {

    private List<LfDeclaration> metaVariables = new ArrayList<LfDeclaration>();

    public LfDeclarationStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfDeclaration declareMeta(String name) {
        LfDeclaration declaration = LightLfDeclaration.declareMeta(this, name);
        // add(declaration);
        metaVariables.add(declaration);
        return declaration;
    }

    public LfDeclaration getDeclaration() {
        return findChildByClass(LfDeclaration.class);
    }

    @Override
    public Icon getIcon(int flags) {
        /*
        RowIcon baseIcon = createLayeredIcon(TwelfIcons.LF_DECLARATION_STATEMENT, ElementPresentationUtil.getFlags(this, false));
        return ElementPresentationUtil.addVisibilityIcon(this, flags, baseIcon);
        */
        return TwelfIcons.LF_DECLARATION_STATEMENT;
    }

    public LfDeclaration[] getMetaVariables() {
        return metaVariables.toArray(new LfDeclaration[metaVariables.size()]);
    }
}
