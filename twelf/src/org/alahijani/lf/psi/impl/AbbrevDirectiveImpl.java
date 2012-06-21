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

    private final Map<String, LfMetaVariable> metaVariables = new LinkedHashMap<String, LfMetaVariable>();

    public AbbrevDirectiveImpl(@NotNull ASTNode node) {
        super(node);
        assert getDirectiveName().equals(TwelfTokenType.D_ABBREV);
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
        return TwelfIcons.ABBREV_DIRECTIVE;
    }
}
