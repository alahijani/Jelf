package org.alahijani.lf.psi.xref;

import com.intellij.codeInsight.lookup.LookupElement;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class LfLookupItem extends LookupElement {

    private LfDeclaration declaration;
    private int order = Integer.MAX_VALUE;

    public LfLookupItem(LfDeclaration declaration) {
        this.declaration = declaration;
    }

    public LfLookupItem(LfDeclaration declaration, int order) {
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

    public LfDeclaration getDeclaration() {
        return declaration;
    }
}
