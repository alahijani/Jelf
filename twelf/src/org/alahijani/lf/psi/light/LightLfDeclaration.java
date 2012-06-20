package org.alahijani.lf.psi.light;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.psi.impl.source.tree.Factory;
import com.intellij.psi.impl.source.tree.SharedImplUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.CharTable;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.lang.Twelf;
import org.alahijani.lf.lang.TwelfElementType;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public abstract class LightLfDeclaration extends LightElement implements LfDeclaration {

    protected final LfIdentifier nameIdentifier;
    protected final LfTerm type;
    protected final ASTNode node;
    protected final PsiElement parent;

    public LightLfDeclaration(PsiElement parent, String name, LfTerm type, LfTerm value) {
        super(parent.getManager(), Twelf.INSTANCE);
        this.nameIdentifier = new LightIdentifier(parent.getManager(), name);
        this.type = type;

        CharTable charTable = SharedImplUtil.findCharTableByTree(parent.getNode());
        this.node = Factory.createSingleLeafElement(TwelfTokenType.PLACEHOLDER, "", charTable, this.getManager());  // todo needed?

        this.parent = parent;
        // setNavigationElement(parent);    this has undesirable side effects
    }

    @Override
    public ASTNode getNode() {
        return node;
    }

    public abstract IElementType getTokenType();

    public String getText() {
        StringBuilder builder = new StringBuilder(getName());
        if (type != null) {
            builder.append(": ").append(type.getText());
        }
        return builder.toString();
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TwelfElementVisitor) {
            ((TwelfElementVisitor) visitor).visitDeclaration(this);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public PsiFile getContainingFile() {
        return parent.getContainingFile();
    }

    @Override
    public int getTextOffset() {
        return parent.getTextOffset();
    }

    public String toString() {
        return "variable '" + getName() + "'";
    }

    @NotNull
    public String getName() {
        return nameIdentifier.getText();
    }

    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        nameIdentifier.setText(name);
        return this;
    }

    public LfIdentifier getNameIdentifier() {
        return nameIdentifier;
    }

    public LfTerm getType() {
        return type;
    }

    public LfTypeElement getTypeElement() {
        return null;  // todo
    }

    public static LfLocalVariable declareAnonymousLocal(final PsiElement parent, @Nullable final LfTerm type) {
        return new AnonymousLfLocalVariableImpl(parent, type);
    }

    public static LfMetaVariable declareMeta(final PsiElement parent, final String name) {
        return new LfMetaVariableImpl(parent, name);
    }

    private static class LfMetaVariableImpl extends LightLfDeclaration implements LfMetaVariable {
        public LfMetaVariableImpl(PsiElement parent, String name) {
            super(parent, name, null, null);
        }

        @Override
        public boolean canNavigate() {
            return true;
        }

        @Override
        public PsiElement copy() {
            return new LfMetaVariableImpl(parent, getName());
        }

        @Override
        public IElementType getTokenType() {
            return TwelfElementType.LF_LOCAL_VARIABLE;
        }
    }

    private static class AnonymousLfLocalVariableImpl extends LightLfDeclaration implements LfLocalVariable {

        public AnonymousLfLocalVariableImpl(PsiElement parent, LfTerm type) {
            super(parent, "_", type, null);
        }

        @Override
        public boolean canNavigate() {
            return false;
        }

        @Override
        public PsiElement copy() {
            return new AnonymousLfLocalVariableImpl(parent, type);
        }

        @Override
        public IElementType getTokenType() {
            return TwelfElementType.LF_META_VARIABLE;
        }
    }
}
