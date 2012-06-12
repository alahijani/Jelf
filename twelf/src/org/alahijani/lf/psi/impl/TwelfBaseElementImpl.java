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
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.TwelfElement;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author Ali Lahijani
 */
public class TwelfBaseElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements TwelfElement {

    protected TwelfBaseElementImpl(final T stub, IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public TwelfBaseElementImpl(final ASTNode node) {
        super(node);
    }

    public void removeElements(PsiElement[] elements) throws IncorrectOperationException {
        ASTNode parentNode = getNode();
        for (PsiElement element : elements) {
            if (element.isValid()) {
                ASTNode node = element.getNode();
                if (node == null || node.getTreeParent() != parentNode) {
                    throw new IncorrectOperationException();
                }
                parentNode.removeChild(node);
            }
        }
    }

    @Override
    public PsiElement getParent() {
        return getParentByStub();
    }

    public <T extends TwelfElement> Iterable<T> childrenOfType(final TokenSet tokSet) {
        return new Iterable<T>() {

            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    private ASTNode findChild(ASTNode child) {
                        if (child == null) return null;

                        if (tokSet.contains(child.getElementType())) return child;

                        return findChild(child.getTreeNext());
                    }

                    PsiElement first = getFirstChild();

                    ASTNode n = first == null ? null : findChild(first.getNode());

                    public boolean hasNext() {
                        return n != null;
                    }

                    public T next() {
                        if (n == null) return null;
                        else {
                            final ASTNode res = n;
                            n = findChild(n.getTreeNext());
                            return (T) res.getPsi();
                        }
                    }

                    public void remove() {
                    }
                };
            }
        };
    }

    public void accept(TwelfElementVisitor visitor) {
        visitor.visitElement(this);
    }

    public void acceptChildren(TwelfElementVisitor visitor) {
        PsiElement child = getFirstChild();
        while (child != null) {
            if (child instanceof TwelfElement) {
                child.accept(visitor);
            }

            child = child.getNextSibling();
        }
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

}
