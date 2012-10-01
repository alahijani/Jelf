package org.alahijani.lf.psi.stubs.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * <code>LfGlobalVariable</code>s indexed by name
 *
 * @author Ali Lahijani
 */
public class LfGlobalVariableIndex extends StringStubIndexExtension<LfGlobalVariable> {

    public static final StubIndexKey<String, LfGlobalVariable> KEY = StubIndexKey.createIndexKey("twelf.global");

    public static Collection<LfGlobalVariable> getLfGlobalVariables(String name,
                                                                    GlobalSearchScope scope,
                                                                    @NotNull Project project) {
        return StubIndex.getInstance().get(KEY, name, project, scope);
    }

    @NotNull
    public StubIndexKey<String, LfGlobalVariable> getKey() {
        return KEY;
    }

}
