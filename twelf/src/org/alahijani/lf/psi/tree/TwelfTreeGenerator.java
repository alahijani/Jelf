package org.alahijani.lf.psi.tree;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.tree.Factory;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.impl.source.tree.TreeGenerator;
import com.intellij.util.CharTable;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.light.TwelfLightElement;

/**
 * @author Ali Lahijani
 */
public class TwelfTreeGenerator implements TreeGenerator {

    public TreeElement generateTreeFor(PsiElement original, CharTable table, PsiManager manager) {
        if (original instanceof TwelfLightElement) {
            return Factory.createSingleLeafElement(TwelfTokenType.PLACEHOLDER, "", table, manager);
        }
        return null;
    }
}
