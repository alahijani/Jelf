package org.alahijani.lf.psi.tree;

import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.Twelf;
import org.jetbrains.annotations.NonNls;

/**
 * @author Ali Lahijani
 */
public class ITwelfElementType extends IElementType {
    public ITwelfElementType(@NonNls String debugName) {
        super(debugName, Twelf.INSTANCE);
    }
}
