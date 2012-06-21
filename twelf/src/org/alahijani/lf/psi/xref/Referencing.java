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
import com.intellij.psi.search.GlobalSearchScope;
import org.alahijani.lf.lexer.TwelfLexer;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.stubs.index.LfGlobalVariableIndex;
import org.alahijani.lf.psi.stubs.index.TwelfConfigFileIndex;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Ali Lahijani
 */
public class Referencing {

    @NotNull
    public static ResolveResult[] multiResolveTarget(LfIdentifierReference reference) {
        @NotNull String name = reference.getText();

        if (TwelfLexer.isAnonymousIdentifier(name)) {
            return ResolveResult.EMPTY_ARRAY;    // TODO implement term reconstruction
        }
        boolean isUppercase = TwelfLexer.isUppercaseIdentifier(name);

        LfMetaVariable meta = null;
        LfDeclaration beingDeclared = null;

        for (PsiElement element = reference; true; element = element.getParent()) {

            if (element instanceof LocalVariableBinder) {
                LocalVariableBinder binder = (LocalVariableBinder) element;
                LfLocalVariable local = binder.getBoundDeclaration();
                if (local != beingDeclared && name.equals(local.getName())) {
                    return createValid(local);
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
                /**
                 * should also check TwelfStatement, so no continue
                 */
            }
            if (element instanceof TwelfStatement) {
                TwelfFile userFile = (TwelfFile) element.getContainingFile();

                LfGlobalVariable sameFile = getLastDeclaration(name, userFile, element.getTextOffset());
                if (sameFile != null) {
                    return createValid(sameFile);
                }

                Collection<TwelfConfigFile> configFiles =
                        TwelfConfigFileIndex.getContainingTwelfConfigFiles(userFile.getName(), element.getResolveScope());

                if (configFiles.isEmpty()) {
                    if (meta == null) {
                        return ResolveResult.EMPTY_ARRAY;
                    } else {
                        return createValid(meta);
                    }
                }

                ArrayList<PsiElement> results = new ArrayList<PsiElement>();
                for (TwelfConfigFile configFile : configFiles) {
                    LfDeclaration lastDeclaration = getLastDeclaration(name, configFile, userFile);
                    if (lastDeclaration == null && meta != null) {
                        lastDeclaration = meta;
                    }
                    results.add(lastDeclaration);
                }
                if (!results.isEmpty()) {
                    if (areElementsEquivalent(results)) {
                        return createValid(results.get(0));
                    } else {
                        return createInvalid(results);
                    }
                } else {
                    return ResolveResult.EMPTY_ARRAY;
                }
            }
            if (element == null || element instanceof PsiFile) return ResolveResult.EMPTY_ARRAY;
        }
    }

    private static ResolveResult[] createValid(PsiElement element) {
        return new ResolveResult[]{new PsiElementResolveResult(element)};
    }

    private static ResolveResult[] createInvalid(@NotNull Collection<? extends PsiElement> elements) {
        if (elements.isEmpty()) return ResolveResult.EMPTY_ARRAY;

        final ResolveResult[] results = new ResolveResult[elements.size()];
        int i = 0;
        for (PsiElement element : elements) {
            results[i++] = new PsiElementResolveResult(element, false);
        }
        return results;
    }

    private static boolean areElementsEquivalent(@NotNull Collection<? extends PsiElement> elements) {
        if (elements.isEmpty()) return true;    // vacuously

        Iterator<? extends PsiElement> iterator = elements.iterator();
        PsiElement curr = iterator.next();
        while (iterator.hasNext()) {
            PsiElement next = iterator.next();
            if (!PsiEquivalenceUtil.areElementsEquivalent(curr, next)) {
                return false;
            }
            curr = next;
        }

        return true;
    }

    private static LfGlobalVariable getLastDeclaration(String name, TwelfConfigFile configFile, TwelfFile beforeFile) {
        TwelfFileReference[] memberFiles = configFile.getMemberFiles();
        LfGlobalVariable result = null;
        for (TwelfFileReference member : memberFiles) {
            TwelfFile file = member.resolve();
            if (file == null) {
                continue;
            }
            if (PsiEquivalenceUtil.areElementsEquivalent(file, beforeFile)) {
                break;
            }
            LfGlobalVariable lastDeclaration = getLastDeclaration(name, file, Integer.MAX_VALUE);
            if (lastDeclaration != null) {
                result = lastDeclaration;
            }
        }
        return result;
    }

    private static LfGlobalVariable getLastDeclaration(String name, TwelfFile file, int beforeOffset) {
        Collection<LfGlobalVariable> otherFileGlobals
                = LfGlobalVariableIndex.getLfGlobalVariables(name, GlobalSearchScope.fileScope(file));

        LfGlobalVariable best = null;
        int bestOffset = -1;

        for (LfGlobalVariable candidate : otherFileGlobals) {
            int offset = candidate.getTextOffset();
            if (bestOffset < offset && offset < beforeOffset) {
                best = candidate;
                bestOffset = offset;
            }
        }

        return best;
    }

    @NotNull
    public static ResolveResult[] multiResolveTarget_(TwelfIdentifierReference<LfDeclaration> reference) {
        @NotNull String name = reference.getText();

        if (TwelfLexer.isAnonymousIdentifier(name)) {
            return new ResolveResult[0];    // TODO implement term reconstruction
        }
        boolean isUppercase = TwelfLexer.isUppercaseIdentifier(name);

        LfMetaVariable meta = null;
        LfDeclaration beingDeclared = null;

        for (PsiElement element = reference; true; element = element.getParent()) {

            if (element instanceof LocalVariableBinder) {
                LocalVariableBinder binder = (LocalVariableBinder) element;
                LfLocalVariable local = binder.getBoundDeclaration();
                if (local != beingDeclared && name.equals(local.getName())) {
                    return createValid(local);
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
                    results.add(new PsiElementResolveResult(global, canReference((TwelfStatement) element, global)));
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
                TwelfConfigFileIndex.getContainingTwelfConfigFiles(twelf.getName(), twelf.getResolveScope());
        boolean can = true;
        for (TwelfConfigFile configFile : configFiles) {
            can &= configFile.canReference(twelf, global.getContainingFile());
        }
        return can;
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



