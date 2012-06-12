package org.alahijani.lf.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;

/**
 * @author Ali Lahijani
 */
abstract public class TwelfStructureViewElement<T extends PsiElement> implements StructureViewTreeElement {
    final protected T myElement;

    public TwelfStructureViewElement(T element) {
        myElement = element;
    }

    public Object getValue() {
        return myElement.isValid() ? myElement : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwelfStructureViewElement that = (TwelfStructureViewElement) o;

        if (myElement != null ? !myElement.equals(that.myElement) : that.myElement != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return myElement != null ? myElement.hashCode() : 0;
    }

    public void navigate(boolean b) {
        ((Navigatable) myElement).navigate(b);
    }

    public boolean canNavigate() {
        return ((Navigatable) myElement).canNavigate();
    }

    public boolean canNavigateToSource() {
        return ((Navigatable) myElement).canNavigateToSource();
    }

    public TreeElement[] getChildren() {
        return TwelfStructureViewElement.EMPTY_ARRAY;
    }
}
