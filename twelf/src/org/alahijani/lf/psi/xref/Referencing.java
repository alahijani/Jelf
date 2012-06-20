package org.alahijani.lf.psi.xref;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionSorter;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.intellij.psi.PsiElement;
import org.alahijani.lf.lexer.TwelfLexer;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ali Lahijani
 */
public class Referencing {

/*
    @NotNull
    public static ResolveResult[] multiResolveTarget(LfIdentifierReference reference) {
        @NotNull String name = reference.getText();
        if (TwelfLexer.isAnonymousIdentifier(name)) {
            return new ResolveResult[0];    // TODO implement term reconstruction
        }
        boolean isUppercase = TwelfLexer.isUppercaseIdentifier(name);

        MetaVariableBinder metaVariableBinder = null;
        LfDeclaration beingDeclared = null;
        PsiElement element = reference;

        for (; ; element = element.getParent()) {

            if (element instanceof LocalVariableBinder) {
                LocalVariableBinder binder = (LocalVariableBinder) element;
                LfDeclaration declaration = binder.getBoundDeclaration();
                if (declaration != beingDeclared && name.equals(declaration.getName())) {
                    return new ResolveResult[]{new PsiElementResolveResult(declaration)};
                }
                continue;
            }
            if (element instanceof LfDeclaration) {
                beingDeclared = (LfDeclaration) element;
                continue;
            }
            if (isUppercase && element instanceof MetaVariableBinder) {
                metaVariableBinder = (MetaVariableBinder) element;
                LfDeclaration meta = metaVariableBinder.getMeta(name);
                if (meta != null) {
                    return meta;
                }
*/
                /**
                 * should also check TwelfStatement, so no continue
                 */
/*
            }
            if (element instanceof TwelfStatement) {
                Map<String, LfGlobalVariable> globals = ((TwelfStatement) element).getGlobalVariablesBefore();
                LfDeclaration global = globals.get(name);
                if (global != null) {
                    return global;
                }

                if (metaVariableBinder != null) {
                    // assert isUppercase;
                    return metaVariableBinder.declareMeta(name);
                }
            }
            if (element == null) return null;
        }
    }
*/

    @Nullable
    public static LfDeclaration resolveTarget(LfIdentifierReference reference) {

        @NotNull String name = reference.getText();
        if (TwelfLexer.isAnonymousIdentifier(name)) {
            return null;    // TODO implement term reconstruction
        }
        boolean isUppercase = TwelfLexer.isUppercaseIdentifier(name);

        MetaVariableBinder metaVariableBinder = null;
        LfDeclaration beingDeclared = null;
        PsiElement element = reference;

        for (; ; element = element.getParent()) {

            if (element instanceof LocalVariableBinder) {
                LocalVariableBinder binder = (LocalVariableBinder) element;
                LfDeclaration declaration = binder.getBoundDeclaration();
                if (declaration != beingDeclared && name.equals(declaration.getName())) {
                    return declaration;
                }
                continue;
            }
            if (element instanceof LfDeclaration) {
                beingDeclared = (LfDeclaration) element;
                continue;
            }
            if (isUppercase && element instanceof MetaVariableBinder) {
                metaVariableBinder = (MetaVariableBinder) element;
                LfDeclaration meta = metaVariableBinder.getMeta(name);
                if (meta != null) {
                    return meta;
                }
                /**
                 * should also check TwelfStatement, so no continue
                 */
            }
            if (element instanceof TwelfStatement) {
                Map<String, LfGlobalVariable> globals = ((TwelfStatement) element).getGlobalVariablesBefore();
                LfDeclaration global = globals.get(name);
                if (global != null) {
                    return global;
                }

                if (metaVariableBinder != null) {
                    // assert isUppercase;
                    return metaVariableBinder.declareMeta(name);
                }
            }
            if (element == null) return null;
        }

    }

    public static void lookup(PsiElement element, CompletionResultSet result) {

        int order = 0;
        result = result.withRelevanceSorter(ByReverseOrderOfDeclaration);

        LfDeclaration beingDeclared = null;

        MetaVariableBinder metaVariableBinder = null;
        for (; ; element = element.getParent()) {

            if (element instanceof LocalVariableBinder) {
                LocalVariableBinder binder = (LocalVariableBinder) element;
                LfDeclaration declaration = binder.getBoundDeclaration();

                if (beingDeclared != declaration && !declaration.getNameIdentifier().isAnonymous()) {
                    result.addElement(LookupElementBuilder.create(declaration));
                    result.addElement(new LfLookupItem(declaration, order++));
                }
                continue;
            }
            if (element instanceof LfDeclaration) {
                beingDeclared = (LfDeclaration) element;
                continue;
            }
            if (element instanceof MetaVariableBinder) {
                metaVariableBinder = (MetaVariableBinder) element;

                /**
                 * should also check TwelfStatement, so no continue
                 */
            }
            if (element instanceof TwelfStatement) {
                Map<String, LfGlobalVariable> globals = ((TwelfStatement) element).getGlobalVariablesBefore();

                if (metaVariableBinder != null) {
                    Set<String> metaCandidates = getMetaCandidates(metaVariableBinder);
                    for (String candidate : metaCandidates) {
                        if (!globals.containsKey(candidate)) {
                            result.addElement(new LfLookupItem(metaVariableBinder.declareMeta(candidate), order++));
                        }
                    }
                }

                for (LfDeclaration global : globals.values()) {
                    if (!global.getNameIdentifier().isAnonymous()) {
                        result.addElement(new LfLookupItem(global, order++));
                    }
                }

                return;
            }
            if (element == null) return;
        }

    }

    private static Set<String> getMetaCandidates(MetaVariableBinder metaVariableBinder) {
        final Set<String> metaCandidates = new HashSet<String>();
        metaVariableBinder.accept(new TwelfElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                element.acceptChildren(this);
            }

            @Override
            public void visitReference(LfIdentifierReference reference) {
                LfIdentifier identifier = reference.getIdentifier();
                if (identifier.isUppercase() && !identifier.isAnonymous()) {
                    metaCandidates.add(identifier.getText());
                }
            }
        });
        return metaCandidates;
    }

    private static final CompletionSorter ByReverseOrderOfDeclaration =
            CompletionSorter.emptySorter().weigh(new LookupElementWeigher("Twelf.byReverseOrderOfDeclaration") {
                @NotNull
                @Override
                public Comparable weigh(@NotNull LookupElement lookupElement) {
                    if (lookupElement instanceof LfLookupItem) {
                        return ((LfLookupItem) lookupElement).getOrder();
                    }
                    return 0;
                }
            });

    public static void rename(LfIdentifier identifier, String newElementName) {
        if (identifier != null) {
            identifier.setText(newElementName);
        }
    }
}



