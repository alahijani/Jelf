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

    private final Map<String, LfMetaVariable> metaVariables = new LinkedHashMap<String, LfMetaVariable>();

    public LfDeclarationStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public LfMetaVariable getProvisionalMeta(String name) {
        LfMetaVariable metaVariable = metaVariables.get(name);
        if (metaVariable != null) {
            return metaVariable;
        }

        LfMetaVariable candidate = LightLfDeclaration.createMeta(this, name);
        synchronized (metaVariables) {
            LfMetaVariable doubleCheck = metaVariables.get(name);
            if (doubleCheck == null) {      // if still null
                metaVariables.put(name, candidate);
                // add(declaration);
                return candidate;
            } else {
                // return the newly added object from the other thread
                return doubleCheck;
            }
        }
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
