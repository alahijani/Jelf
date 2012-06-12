package org.alahijani.lf.psi.light;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.Twelf;
import org.alahijani.lf.TwelfElementType;
import org.alahijani.lf.lexer.TwelfLexer;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.LfIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class LightIdentifier extends LightElement implements LfIdentifier {

    private String myText;

    public LightIdentifier(PsiManager manager, String text) {
        super(manager, Twelf.INSTANCE);
        myText = text;
    }

    public IElementType getTokenType() {
        return TwelfElementType.IDENTIFIER;
        // return TwelfTokenType.IDENT;
    }

    public String getText() {
        return myText;
    }

    public void setText(String text) {
        this.myText = text;
    }

    public boolean isUppercase() {
        return TwelfLexer.isUppercaseIdentifier(getText());
    }

    public boolean isAnonymous() {
        return TwelfLexer.isAnonymousIdentifier(getText());
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TwelfElementVisitor) {
            ((TwelfElementVisitor) visitor).visitIdentifier(this);
        } else {
            visitor.visitElement(this);
        }
    }

    public PsiElement copy() {
        return new LightIdentifier(getManager(), myText);
    }

    public String toString() {
        return "identifier '" + getText() + "'";
    }

    public PsiType getType() {
        return null;
    }

}
