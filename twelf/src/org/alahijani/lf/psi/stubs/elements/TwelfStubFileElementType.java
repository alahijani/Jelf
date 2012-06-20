package org.alahijani.lf.psi.stubs.elements;

import com.intellij.psi.tree.IStubFileElementType;
import org.alahijani.lf.lang.Twelf;
import org.alahijani.lf.psi.stubs.TwelfFileStub;

/**
 * @author Ali Lahijani
 */
public class TwelfStubFileElementType extends IStubFileElementType<TwelfFileStub> {

    public TwelfStubFileElementType() {
        super("Twelf file", Twelf.INSTANCE);
    }

    public String getExternalId() {
        return "twelf.FILE";
    }

}
