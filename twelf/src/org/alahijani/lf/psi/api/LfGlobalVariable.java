package org.alahijani.lf.psi.api;

import com.intellij.psi.StubBasedPsiElement;
import org.alahijani.lf.psi.stubs.LfGlobalVariableStub;

/**
 * @author Ali Lahijani
 */
public interface LfGlobalVariable extends LfDeclaration, StubBasedPsiElement<LfGlobalVariableStub> {

    LfTerm getValue();

}
