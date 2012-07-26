package org.alahijani.lf.psi.api;

import com.intellij.psi.PsiElement;

/**
 * @author Ali Lahijani
 */
public interface TwelfBaseElement extends PsiElement {

    TwelfBaseElement getVirtualParent();

    Iterable<? extends TwelfBaseElement> getVirtualParentChain();
            // default TwelfPsiUtil.getVirtualParentChain(TwelfBaseElement);

}
