package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.alahijani.lf.psi.api.GlobalVariableBinder;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.TwelfFile;
import org.alahijani.lf.psi.api.TwelfStatement;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfStatementImpl extends TwelfElementImpl implements TwelfStatement {

    private Map<String,LfDeclaration> globalVariablesBefore;

    public TwelfStatementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public TwelfFile getFile() {
        return (TwelfFile) getParent();
    }

    public Map<String, LfDeclaration> getGlobalVariablesBefore() {
        if (globalVariablesBefore == null) {
            globalVariablesBefore = findGlobalVariablesBefore();
        }

        return globalVariablesBefore;
    }

    private Map<String, LfDeclaration> findGlobalVariablesBefore() {
        Map<String, LfDeclaration> globalVariablesBefore = new LinkedHashMap<String, LfDeclaration>();

        PsiElement element = this;
        do {
            element = element.getPrevSibling();
            if (element instanceof GlobalVariableBinder) {
                LfDeclaration declaration = ((GlobalVariableBinder) element).getDeclaration();
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
