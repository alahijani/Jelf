package org.alahijani.lf.structure;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfItemPresentation<E extends PsiElement> implements ItemPresentation {
    protected final E myElement;

    protected TwelfItemPresentation(E myElement) {
        this.myElement = myElement;
    }

    @Nullable
    public String getLocationString() {
        return null;
    }

    @Nullable
    public Icon getIcon(boolean open) {
        return myElement.getIcon(Iconable.ICON_FLAG_OPEN);
    }

    @Nullable
    public TextAttributesKey getTextAttributesKey() {
        return null;
    }
}
