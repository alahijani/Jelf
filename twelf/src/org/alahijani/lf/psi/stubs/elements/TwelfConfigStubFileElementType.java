package org.alahijani.lf.psi.stubs.elements;

import com.intellij.psi.tree.IStubFileElementType;
import org.alahijani.lf.lang.TwelfConfig;
import org.alahijani.lf.psi.stubs.TwelfFileStub;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigStubFileElementType extends IStubFileElementType<TwelfFileStub> {

    public TwelfConfigStubFileElementType() {
        super("Twelf config file", TwelfConfig.INSTANCE);
    }

    public String getExternalId() {
        return "twelfConfig.FILE";
    }

}
