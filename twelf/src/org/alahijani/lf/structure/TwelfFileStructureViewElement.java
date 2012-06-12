package org.alahijani.lf.structure;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import org.alahijani.lf.psi.api.GlobalVariableBinder;
import org.alahijani.lf.psi.api.TwelfFile;
import org.alahijani.lf.psi.api.TwelfStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ali Lahijani
 */
public class TwelfFileStructureViewElement extends TwelfStructureViewElement<TwelfFile> {
    public TwelfFileStructureViewElement(TwelfFile twelfFile) {
        super(twelfFile);
    }

    public ItemPresentation getPresentation() {
        return new TwelfItemPresentation<TwelfFile>(myElement) {
            public String getPresentableText() {
                return myElement.getName();
            }
        };
    }

    public TreeElement[] getChildren() {
        List<TwelfStructureViewElement> children = new ArrayList<TwelfStructureViewElement>();

        for (TwelfStatement statement : myElement.getStatements()) {
            if (statement instanceof GlobalVariableBinder) {
                children.add(new TwelfDeclaringStatementStructureViewElement((GlobalVariableBinder) statement));
            }
        }

        return children.toArray(new TwelfStructureViewElement[children.size()]);
    }
}
