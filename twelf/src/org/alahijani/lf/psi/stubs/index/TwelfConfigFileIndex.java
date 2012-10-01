package org.alahijani.lf.psi.stubs.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import org.alahijani.lf.psi.api.TwelfConfigFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * <code>TwelfConfigFile</code>s indexed by Twelf file names they contain
 *
 * @author Ali Lahijani
 */
public class TwelfConfigFileIndex extends StringStubIndexExtension<TwelfConfigFile> {

    public static final StubIndexKey<String, TwelfConfigFile> KEY = StubIndexKey.createIndexKey("twelf.cfg");

    public static Collection<TwelfConfigFile> getContainingTwelfConfigFiles(String elfFileName,
                                                                            GlobalSearchScope scope,
                                                                            @NotNull Project project) {
        return StubIndex.getInstance().get(KEY, elfFileName, project, scope);
    }

    @NotNull
    public StubIndexKey<String, TwelfConfigFile> getKey() {
        return KEY;
    }

}
