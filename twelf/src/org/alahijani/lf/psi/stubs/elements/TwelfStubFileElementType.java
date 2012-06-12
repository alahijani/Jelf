package org.alahijani.lf.psi.stubs.elements;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IStubFileElementType;
import org.alahijani.lf.psi.stubs.TwelfFileStub;

/**
 * @author Ali Lahijani
 */
public class TwelfStubFileElementType extends IStubFileElementType<TwelfFileStub> {

    public TwelfStubFileElementType(Language language) {
        super(language);
    }

    @Override
    public int getStubVersion() {
        // what?!!
        return super.getStubVersion() + 6;
    }

    public String getExternalId() {
        return "twelf.FILE";
    }

}
