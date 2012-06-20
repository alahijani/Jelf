package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import org.alahijani.lf.TwelfIcons;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.GlobalVariableBinder;
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
public class AbbrevDirectiveImpl extends TwelfDirectiveImpl implements GlobalVariableBinder {

    private Map<String, LfMetaVariable> metaVariables = new LinkedHashMap<String, LfMetaVariable>();

    public AbbrevDirectiveImpl(@NotNull ASTNode node) {
        super(node);
        assert getDirectiveName().equals(TwelfTokenType.D_ABBREV);
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
        return TwelfIcons.ABBREV_DIRECTIVE;
    }
}
