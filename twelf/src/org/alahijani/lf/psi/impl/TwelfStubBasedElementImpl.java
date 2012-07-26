package org.alahijani.lf.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.CheckUtil;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.psi.impl.source.tree.ChangeUtil;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.psi.api.TwelfBaseElement;
import org.alahijani.lf.psi.util.TwelfPsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfStubBasedElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements TwelfBaseElement {

    protected TwelfStubBasedElementImpl(final T stub, IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public TwelfStubBasedElementImpl(final ASTNode node) {
        super(node);
    }


    @Override
    public PsiElement getParent() {
        return getParentByStub();
    }

    public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
        CompositeElement treeElement = calcTreeElement();
        assert treeElement.getTreeParent() != null;
        CheckUtil.checkWritable(this);
        TreeElement elementCopy = ChangeUtil.copyToElement(newElement);
        treeElement.getTreeParent().replaceChildInternal(treeElement, elementCopy);
        elementCopy = ChangeUtil.decodeInformation(elementCopy);
        return SourceTreeToPsiMap.treeElementToPsi(elementCopy);
    }

    protected CompositeElement calcTreeElement() {
        return (CompositeElement) getNode();
    }

    public TwelfBaseElement getVirtualParent() {
        return (TwelfBaseElement) getParent();
    }

    public Iterable<? extends TwelfBaseElement> getVirtualParentChain() {
        return TwelfPsiUtil.getVirtualParentChain(this);
    }
}
