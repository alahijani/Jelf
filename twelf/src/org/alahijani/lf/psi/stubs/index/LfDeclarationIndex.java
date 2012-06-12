package org.alahijani.lf.psi.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.alahijani.lf.psi.api.LfDeclaration;

/**
 * @author Ali Lahijani
 */
public class LfDeclarationIndex extends StringStubIndexExtension<LfDeclaration> {

    public static final StubIndexKey<String, LfDeclaration> KEY = StubIndexKey.createIndexKey("twelf.decl");

    public StubIndexKey<String, LfDeclaration> getKey() {
        return KEY;
    }

}
