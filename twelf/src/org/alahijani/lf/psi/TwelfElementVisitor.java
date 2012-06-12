package org.alahijani.lf.psi;

import com.intellij.psi.PsiElementVisitor;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.LfIdentifier;
import org.alahijani.lf.psi.api.LfIdentifierReference;

/**
 * @author Ali Lahijani
 */
public class TwelfElementVisitor extends PsiElementVisitor {

    public void visitIdentifier(LfIdentifier identifier) {
        visitElement(identifier);
    }

    public void visitDeclaration(LfDeclaration declaration) {
        visitElement(declaration);
    }


    public void visitReference(LfIdentifierReference reference) {
        visitElement(reference);
    }

}
