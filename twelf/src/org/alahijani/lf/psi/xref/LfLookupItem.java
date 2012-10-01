package org.alahijani.lf.psi.xref;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.util.Iconable;
import org.alahijani.lf.psi.api.ReferableElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LfLookupItem extends LookupElement /*implements TypedLookupItem*/ {

    private ReferableElement declaration;
    private int order = Integer.MAX_VALUE;

    public LfLookupItem(ReferableElement declaration) {
        this.declaration = declaration;
    }

    public LfLookupItem(ReferableElement declaration, int order) {
        this.declaration = declaration;
        this.order = order;
    }

    @NotNull
    @Override
    public String getLookupString() {
        return declaration.getName();
    }

    public int getOrder() {
        return order;
    }

    public ReferableElement getDeclaration() {
        return declaration;
    }

    public void renderElement(LookupElementPresentation presentation) {
        presentation.setItemText(declaration.getName());
        presentation.setIcon(declaration.getIcon(Iconable.ICON_FLAG_VISIBILITY));
        presentation.setTypeText(declaration.getTypeText());
    }
}
