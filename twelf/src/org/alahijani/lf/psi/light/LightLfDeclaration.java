package org.alahijani.lf.psi.light;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.Factory;
import com.intellij.psi.impl.source.tree.SharedImplUtil;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.CharTable;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.editor.TwelfHighlighterColors;
import org.alahijani.lf.formatter.TwelfFormatter;
import org.alahijani.lf.lang.TwelfElementType;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.structure.TwelfItemPresentation;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public abstract class LightLfDeclaration<ParentType extends TwelfElement>
        extends TwelfLightElement<ParentType> implements LfDeclaration {

    protected final TwelfIdentifier nameIdentifier;
    protected final LfTerm type;
    protected final ASTNode node;

    public LightLfDeclaration(ParentType virtualParent, String name, LfTerm type, @Nullable LfTerm value) {
        super(virtualParent);
        this.nameIdentifier = new LightIdentifier(this, name);
        this.type = type;

        CharTable charTable = SharedImplUtil.findCharTableByTree(virtualParent.getNode());
        this.node = null;
                Factory.createSingleLeafElement(TwelfTokenType.PLACEHOLDER, "", charTable, this.getManager());  // todo needed?

        // setNavigationElement(virtualParent);    this has undesirable side effects
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
        return getVirtualParent().getContainingFile();
    }

    public String toString() {
        return "variable '" + getName() + "'";
    }

    @NotNull
    public String getName() {
        return nameIdentifier.getText();
    }

    public String getTypeText() {
        return TwelfFormatter.format(getType());
    }

    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        nameIdentifier.setText(name);
        return this;
    }

    public TwelfIdentifier getNameIdentifier() {
        return nameIdentifier;
    }

    public LfTerm getType() {
        return type;
    }

    public LfTypeElement getTypeElement() {
        return null;  // todo
    }

    @NotNull
    public static LfLocalVariable createAnonymousLocal(final LocalVariableBinder parent, @Nullable final LfTerm type) {
        return new AnonymousLfLocalVariableImpl(parent, type);
    }

    @NotNull
    public static LfMetaVariable createMeta(final MetaVariableBinder parent, final String name) {
        return new LfMetaVariableImpl(parent, name);
    }

    private static class LfMetaVariableImpl extends LightLfDeclaration<MetaVariableBinder> implements LfMetaVariable {
        private boolean committed = false;

        public LfMetaVariableImpl(MetaVariableBinder parent, String name) {
            super(parent, name, null, null);
        }

        @Override
        public boolean canNavigate() {
            return true;
        }

        @Override
        public PsiElement copy() {
            return new LfMetaVariableImpl(getVirtualParent(), getName());
        }

        @Override
        public IElementType getTokenType() {
            return TwelfElementType.LF_META_VARIABLE;
        }

        public boolean isCommitted() {
            return committed;
        }

        public synchronized void commit() {
            committed = true;
        }

        @Override
        public ItemPresentation getPresentation() {
            return new LightLfDeclarationPresentation(this, TwelfHighlighterColors.LF_META_VARIABLE);
        }

        @NotNull
        @Override
        public SearchScope getUseScope() {
            return new LocalSearchScope(getVirtualParent());
        }
    }

    private static class AnonymousLfLocalVariableImpl extends LightLfDeclaration<LocalVariableBinder> implements LfLocalVariable {

        public AnonymousLfLocalVariableImpl(LocalVariableBinder parent, LfTerm type) {
            super(parent, "_", type, null);
        }

        @Override
        public boolean canNavigate() {
            return false;
        }

        @Override
        public PsiElement copy() {
            return new AnonymousLfLocalVariableImpl(getVirtualParent(), type);
        }

        @Override
        public IElementType getTokenType() {
            return TwelfElementType.LF_LOCAL_VARIABLE;
        }

        @Override
        public ItemPresentation getPresentation() {
            return new LightLfDeclarationPresentation(this, TwelfHighlighterColors.LF_LOCAL_IDENTIFIER);
        }

        @NotNull
        @Override
        public SearchScope getUseScope() {
            return new LocalSearchScope(getVirtualParent(), "Empty scope");
        }
    }

    private static class LightLfDeclarationPresentation extends TwelfItemPresentation<LightLfDeclaration> {

        private final TextAttributesKey textAttributesKey;
        private LightLfDeclaration declaration;

        public LightLfDeclarationPresentation(LightLfDeclaration declaration, TextAttributesKey textAttributesKey) {
            super(declaration);
            this.textAttributesKey = textAttributesKey;
            this.declaration = declaration;
        }

        public String getPresentableText() {
            return declaration.getName();
        }

        @Override
        public TextAttributesKey getTextAttributesKey() {
            return textAttributesKey;
        }

    }

}
