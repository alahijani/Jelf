package org.alahijani.lf.psi.xref;

import com.intellij.codeInsight.PsiEquivalenceUtil;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionSorter;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import org.alahijani.lf.lexer.TwelfLexer;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.stubs.index.LfGlobalVariableIndex;
import org.alahijani.lf.psi.stubs.index.TwelfConfigFileIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Ali Lahijani
 */
public class Referencing {

    @NotNull
    public static ResolveResult[] multiResolveTarget(TwelfIdentifierReference reference) {
        @NotNull String name = reference.getText();

        if (TwelfLexer.isAnonymousIdentifier(name)) {
            return new ResolveResult[0];    // TODO implement term reconstruction
        }
        boolean isUppercase = TwelfLexer.isUppercaseIdentifier(name);

        LfMetaVariable meta = null;
        LfDeclaration beingDeclared = null;
        PsiElement element = reference;

        for (; ; element = element.getParent()) {

            if (element instanceof LocalVariableBinder) {
                LocalVariableBinder binder = (LocalVariableBinder) element;
                LfLocalVariable local = binder.getBoundDeclaration();
                if (local != beingDeclared && name.equals(local.getName())) {
                    return new ResolveResult[]{new PsiElementResolveResult(local)};
                }
                continue;
            }
            if (element instanceof LfDeclaration) {
                beingDeclared = (LfDeclaration) element;
                continue;
            }
            if (isUppercase && element instanceof MetaVariableBinder) {
                MetaVariableBinder binder = (MetaVariableBinder) element;
                meta = binder.getProvisionalMeta(name);
                if (meta.isCommitted()) {                           // todo: buggy optimization!
                    return new ResolveResult[]{new PsiElementResolveResult(meta)};
                }
                /**
                 * should also check TwelfStatement, so no continue
                 */
            }
            if (element instanceof TwelfStatement) {
                Collection<LfGlobalVariable> globals = LfGlobalVariableIndex.getLfGlobalVariables(name, reference.getResolveScope());

                ArrayList<PsiElementResolveResult> results = new ArrayList<PsiElementResolveResult>(globals.size() + 1);
                for (LfGlobalVariable global : globals) {
                    results.add(new PsiElementResolveResult(global, canReference((TwelfStatement)element, global)));
                }
                if (meta != null) {
                    results.add(new PsiElementResolveResult(meta));
                }

                return results.toArray(new ResolveResult[results.size()]);
            }
            if (element == null || element instanceof PsiFile) return ResolveResult.EMPTY_ARRAY;
        }
    }

    private static boolean canReference(TwelfStatement position, LfGlobalVariable global) {
        TwelfFile twelf = position.getContainingFile();
        if (PsiEquivalenceUtil.areElementsEquivalent(twelf, global.getContainingFile())) {
            return position.getTextOffset() > global.getTextOffset();
        }

        Collection<TwelfConfigFile> configFiles =
                TwelfConfigFileIndex.getTwelfConfigFiles(twelf.getName(), twelf.getResolveScope());
        boolean can = true;
        for (TwelfConfigFile configFile : configFiles) {
            can &= configFile.canReference(twelf, global.getContainingFile());
        }
        return can;
    }

    @Nullable
    public static ReferableElement resolveTarget(TwelfIdentifierReference reference) {

        @NotNull String name = reference.getText();
        if (TwelfLexer.isAnonymousIdentifier(name)) {
            return null;    // TODO implement term reconstruction
        }
        boolean isUppercase = TwelfLexer.isUppercaseIdentifier(name);

        LfMetaVariable meta = null;
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
                MetaVariableBinder binder = (MetaVariableBinder) element;
                meta = binder.getProvisionalMeta(name);
                if (meta.isCommitted()) {
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

                if (meta != null) {
                    // assert isUppercase;
                    meta.commit();
                    return meta;
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
                            LfMetaVariable meta = metaVariableBinder.getProvisionalMeta(candidate);
                            meta.commit();
                            result.addElement(new LfLookupItem(meta, order++));
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
            public void visitReference(TwelfIdentifierReference reference) {
                TwelfIdentifier identifier = reference.getIdentifier();
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

    public static void rename(TwelfIdentifier identifier, String newElementName) {
        if (identifier != null) {
            identifier.setText(newElementName);
        }
    }
}



