package org.alahijani.lf.psi.xref;

import com.intellij.codeInsight.PsiEquivalenceUtil;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionSorter;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementWeigher;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FileBasedIndex;
import org.alahijani.lf.lexer.TwelfLexer;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.stubs.index.LfGlobalVariableIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Ali Lahijani
 */
public class Referencing {
    static final Logger LOG = Logger.getInstance("#org.alahijani.lf.psi.xref.Referencing");

//    private static final AnnotationHolder CALLBACK = new AnnotationHolder() {
//        public Annotation createErrorAnnotation(@NotNull PsiElement elt, @Nullable String message) { return null; }
//        public Annotation createErrorAnnotation(@NotNull ASTNode node, @Nullable String message) { return null; }
//        public Annotation createErrorAnnotation(@NotNull TextRange range, @Nullable String message) { return null; }
//        public Annotation createWarningAnnotation(@NotNull PsiElement elt, @Nullable String message) { return null; }
//        public Annotation createWarningAnnotation(@NotNull ASTNode node, @Nullable String message) { return null; }
//        public Annotation createWarningAnnotation(@NotNull TextRange range, @Nullable String message) { return null; }
//        public Annotation createInformationAnnotation(@NotNull PsiElement elt, @Nullable String message) { return null; }
//        public Annotation createInformationAnnotation(@NotNull ASTNode node, @Nullable String message) { return null; }
//        public Annotation createInformationAnnotation(@NotNull TextRange range, String message) { return null; }
//        public Annotation createWeakWarningAnnotation(@NotNull PsiElement psiElement, @Nullable String s) { return null; }
//        public Annotation createWeakWarningAnnotation(@NotNull ASTNode node, @Nullable String s) { return null; }
//        public Annotation createWeakWarningAnnotation(@NotNull TextRange textRange, @Nullable String s) { return null; }
//        public Annotation createInfoAnnotation(@NotNull PsiElement elt, @Nullable String message) { return null; }
//        public Annotation createInfoAnnotation(@NotNull ASTNode node, @Nullable String message) { return null; }
//        public Annotation createInfoAnnotation(@NotNull TextRange range, @Nullable String message) { return null; }
//        public AnnotationSession getCurrentAnnotationSession() { return null; }
//    };

    @NotNull
    public static ResolveResult[] multiResolveTarget(LfIdentifierReference reference) {
        return multiResolveTarget(reference, null);
    }

    @NotNull
    public static ResolveResult[] multiResolveTarget(LfIdentifierReference reference, @Nullable CodeInsightsHolder callback) {
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
                if (local != beingDeclared && local != null && name.equals(local.getName())) {
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

                LfGlobalVariable sameFile = userFile.getLastDeclaration(name, element.getTextOffset(), callback);
                if (sameFile != null) {
                    return createValid(sameFile);
                }

                Collection<TwelfConfigFile> configFiles = userFile.getContainingConfigFiles();

                if (configFiles.isEmpty()) {
                    if (meta == null) {
                        // return ResolveResult.EMPTY_ARRAY;
                        // there is no valid result but these invalid results may be helpful
                        Collection<LfGlobalVariable> allInTheDirectory
                                = LfGlobalVariableIndex.getLfGlobalVariables(name, element.getResolveScope(), element.getProject());
                        return createInvalid(allInTheDirectory);
                    } else {
                        return createValid(meta);
                    }
                }

                ArrayList<PsiElement> results = new ArrayList<PsiElement>();
                for (TwelfConfigFile configFile : configFiles) {
                    LfDeclaration lastDeclaration = configFile.getLastDeclaration(name, userFile, callback);
                    if (lastDeclaration == null) {
                        if (meta != null) {
                            results.add(meta);
                        }
                    } else {
                        results.add(lastDeclaration);
                    }
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
    private static final CompletionSorter ByReverseOrderOfDeclaration_ =
            CompletionSorter.emptySorter().weigh(new LookupElementWeigher("Twelf.byReverseOrderOfDeclaration") {
                @NotNull
                @Override
                public Comparable weigh(@NotNull LookupElement lookupElement) {
                    if (lookupElement instanceof LfLookupItem) {
                        return ((LfLookupItem) lookupElement).getDeclaration().getTextOffset();
                    }
                    return 0;
                }
            });

    public static void rename(TwelfIdentifier identifier, String newElementName) {
        if (identifier != null) {
            identifier.setText(newElementName);
        }
    }

    // -------------------------------------------------------------------------------------------------

    void _(final TwelfElement element, final CompletionResultSet resultSet) {

        final CompletionResultSet result = resultSet.withRelevanceSorter(ByReverseOrderOfDeclaration_);

        final TwelfFile twelfFile = (TwelfFile) element.getContainingFile();
        final PsiManager psiManager = twelfFile.getManager();

        twelfFile.getAllDeclarations(new Processor<LfGlobalVariable>() {
            public boolean process(LfGlobalVariable variable) {
                if (variable.getTextOffset() < element.getTextOffset()) {
                    result.addElement(new LfLookupItem(variable));
                }

                return true;
            }
        });

        twelfFile.getContainingConfigFiles(new FileBasedIndex.ValueProcessor<Integer>() {
            public boolean process(VirtualFile file, Integer index) {
                PsiFile psiFile = psiManager.findFile(file);
                if (!(psiFile instanceof TwelfConfigFile)) {
                    LOG.warn("Expected TwelfConfigFile, found " + psiFile);
                    return true;
                }

                TwelfConfigFile configFile = (TwelfConfigFile) psiFile;

                TwelfFileReference[] memberFiles = configFile.getMemberFiles();
                for (int i = 0; i < index; i++) {
                    TwelfFileReference fileReference = memberFiles[i];
                    TwelfFile resolve = fileReference.resolve();
                    if (resolve != null) {
                        // todo resolve.get
                    }
                }
                return false;
            }
        });
    }

}



