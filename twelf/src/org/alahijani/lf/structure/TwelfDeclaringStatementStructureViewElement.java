package org.alahijani.lf.structure;

import com.intellij.navigation.ItemPresentation;
import org.alahijani.lf.psi.api.GlobalVariableBinder;

/**
 * @author Ali Lahijani
 */
public class TwelfDeclaringStatementStructureViewElement extends TwelfStructureViewElement<GlobalVariableBinder> {
    public TwelfDeclaringStatementStructureViewElement(GlobalVariableBinder statement) {
        super(statement);
    }

    public ItemPresentation getPresentation() {
        return new TwelfItemPresentation<GlobalVariableBinder>(myElement) {
            public String getPresentableText() {
                return myElement.getDeclaration().getName();
            }
        };
    }

}
