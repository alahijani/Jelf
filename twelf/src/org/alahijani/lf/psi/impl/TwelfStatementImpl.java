package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.alahijani.lf.psi.api.*;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfStatementImpl extends TwelfElementImpl implements TwelfStatement {

//    private Map<String, LfGlobalVariable> globalVariablesBefore;

    public TwelfStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public TwelfFile getFile() {
        return (TwelfFile) getParent();
    }

    public Map<String, LfGlobalVariable> getGlobalVariablesBefore() {
//        if (globalVariablesBefore == null) {
//            globalVariablesBefore = findGlobalVariablesBefore();
//        }
//        return globalVariablesBefore;

        return findGlobalVariablesBefore();
    }

    private Map<String, LfGlobalVariable> findGlobalVariablesBefore() {
        Map<String, LfGlobalVariable> globalVariablesBefore = new LinkedHashMap<String, LfGlobalVariable>();

        PsiElement element = this;
        do {
            element = element.getPrevSibling();
            if (element instanceof GlobalVariableBinder) {
                LfGlobalVariable declaration = ((GlobalVariableBinder) element).getDeclaration();
                globalVariablesBefore.put(declaration.getName(), declaration);
            }
            if (element instanceof TwelfStatement) {    // short circuiting for TwelfStatement as an optimization
                globalVariablesBefore.putAll(((TwelfStatement) element).getGlobalVariablesBefore());
                return globalVariablesBefore;
            }

        } while (element != null);

        return globalVariablesBefore;
    }


}
