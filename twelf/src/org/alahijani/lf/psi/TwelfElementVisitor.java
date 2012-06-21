package org.alahijani.lf.psi;

import com.intellij.psi.PsiElementVisitor;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.TwelfIdentifier;
import org.alahijani.lf.psi.api.TwelfIdentifierReference;

/**
 * @author Ali Lahijani
 */
public class TwelfElementVisitor extends PsiElementVisitor {

    public void visitIdentifier(TwelfIdentifier identifier) {
        visitElement(identifier);
    }

    public void visitDeclaration(LfDeclaration declaration) {
        visitElement(declaration);
    }


    public void visitReference(TwelfIdentifierReference reference) {
        visitElement(reference);
    }

}
