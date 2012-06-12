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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ali Lahijani
 */
public class Referencing {

    @Nullable
    public static LfDeclaration resolveTarget(LfIdentifierReference reference) {

        @NotNull String name = reference.getText();
        if (TwelfLexer.isAnonymousIdentifier(name)) {
            return null;    // TODO implement term reconstruction
        }

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
            if (element instanceof MetaVariableBinder) {
                metaVariableBinder = (MetaVariableBinder) element;
                LfDeclaration[] metaVariables = metaVariableBinder.getMetaVariables();
                for (int i = metaVariables.length - 1; i >= 0; i--) {
                    LfDeclaration declaration = metaVariables[i];
                    if (name.equals(declaration.getName())) {
                        return declaration;
                    }
                }
                /**
                 * should also check TwelfStatement, so no continue
                 */
            }
            if (element instanceof TwelfStatement) break;
            if (element == null) return null;
        }

        for (; ; element = element.getPrevSibling()) {

            if (element == null) break;
            if (element instanceof GlobalVariableBinder) {
                LfDeclaration declaration = ((GlobalVariableBinder) element).getDeclaration();
                if (declaration != beingDeclared && name.equals(declaration.getName())) {
                    return declaration;
                }
            }
        }

        if (metaVariableBinder != null) {
            if (TwelfLexer.isUppercaseIdentifier(name)) {
                return metaVariableBinder.declareMeta(name);
            }
        }

        return null;
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
                Map<String, LfDeclaration> globals = getGlobalVariablesBefore((TwelfStatement) element);

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

    private static Map<String, LfDeclaration> getGlobalVariablesBefore(TwelfStatement statement) {
        Map<String, LfDeclaration> result = new LinkedHashMap<String, LfDeclaration>();

        PsiElement element = statement;
        do {
            element = element.getPrevSibling();
            if (element instanceof GlobalVariableBinder) {
                LfDeclaration declaration = ((GlobalVariableBinder) element).getDeclaration();
                result.put(declaration.getName(), declaration);
            }

        } while (element != null);
        return result;
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



