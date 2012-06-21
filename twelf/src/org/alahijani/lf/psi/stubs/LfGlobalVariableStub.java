package org.alahijani.lf.psi.stubs;

import com.intellij.psi.stubs.NamedStub;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public interface LfGlobalVariableStub extends NamedStub<LfGlobalVariable> {

    @NotNull
    String getName();

}
