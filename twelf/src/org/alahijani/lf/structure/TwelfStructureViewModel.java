package org.alahijani.lf.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import org.alahijani.lf.psi.api.TwelfFile;
import org.alahijani.lf.psi.api.TwelfStatement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfStructureViewModel extends TextEditorBasedStructureViewModel {
    private TwelfFile twelfFile;

    public TwelfStructureViewModel(TwelfFile twelfFile) {
        super(twelfFile);
        this.twelfFile = twelfFile;
    }

    @NotNull
    public StructureViewTreeElement getRoot() {
        return new TwelfFileStructureViewElement(twelfFile);
    }

    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @NotNull
    protected Class[] getSuitableClasses() {
        return new Class[]{TwelfStatement.class};
    }

}
