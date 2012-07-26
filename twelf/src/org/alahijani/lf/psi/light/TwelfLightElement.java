package org.alahijani.lf.psi.light;

import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.light.LightElement;
import org.alahijani.lf.lang.Twelf;
import org.alahijani.lf.psi.api.TwelfBaseElement;
import org.alahijani.lf.psi.util.TwelfPsiUtil;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfLightElement<ParentType extends TwelfBaseElement> extends LightElement implements TwelfBaseElement {
    private final ParentType virtualParent;

    public TwelfLightElement(ParentType virtualParent) {
        super(virtualParent.getManager(), Twelf.INSTANCE);
        this.virtualParent = virtualParent;
    }

    public ParentType getVirtualParent() {
        return virtualParent;
    }

    public Iterable<? extends TwelfBaseElement> getVirtualParentChain() {
        return TwelfPsiUtil.getVirtualParentChain(this);
    }

    @Override
    public PsiFile getContainingFile() {
        return virtualParent.getContainingFile();
    }

    @Override
    public int getTextOffset() {
        return virtualParent.getTextOffset();
    }
}
