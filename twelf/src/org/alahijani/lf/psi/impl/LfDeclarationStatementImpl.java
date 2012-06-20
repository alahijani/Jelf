package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.psi.api.LfDeclarationStatement;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.alahijani.lf.psi.api.LfMetaVariable;
import org.alahijani.lf.psi.light.LightLfDeclaration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ali Lahijani
 */
public class LfDeclarationStatementImpl extends TwelfStatementImpl implements LfDeclarationStatement {

    private Map<String, LfMetaVariable> metaVariables = new LinkedHashMap<String, LfMetaVariable>();

    public LfDeclarationStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public LfMetaVariable getMeta(String name) {
        return metaVariables.get(name);
    }

    public LfMetaVariable declareMeta(String name) {
        LfMetaVariable declaration = LightLfDeclaration.declareMeta(this, name);
        // add(declaration);
        metaVariables.put(name, declaration);
        return declaration;
    }

    public LfGlobalVariable getDeclaration() {
        return findChildByClass(LfGlobalVariable.class);
    }

    @Override
    public Icon getIcon(int flags) {
        /*
        RowIcon baseIcon = createLayeredIcon(TwelfIcons.LF_DECLARATION_STATEMENT, ElementPresentationUtil.getFlags(this, false));
        return ElementPresentationUtil.addVisibilityIcon(this, flags, baseIcon);
        */
        return TwelfIcons.LF_DECLARATION_STATEMENT;
    }

}
