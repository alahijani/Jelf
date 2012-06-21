package org.alahijani.lf.psi.stubs;

import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.util.io.StringRef;
import org.alahijani.lf.psi.api.TwelfConfigFile;

import java.util.ArrayList;

/**
 * @author Ali Lahijani
 */
public interface TwelfConfigFileStub extends PsiFileStub<TwelfConfigFile> {

    StringRef getName();

    ArrayList<StringRef> getElfList();

}
