package org.alahijani.lf.search;

import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.QueryExecutor;

/**
 * @author Ali Lahijani
 */
public abstract class TwelfReferencesSearcher implements QueryExecutor<PsiReference, ReferencesSearch.SearchParameters> {

//    public boolean execute(final ReferencesSearch.SearchParameters queryParameters, final Processor<PsiReference> consumer) {
//        if (false) {
//            final PsiElement refElement = queryParameters.getElementToSearch();
//            if (refElement instanceof LfGlobalVariable) {
//                final String name = ((LfGlobalVariable) refElement).getName();
//                if (name == null) return true;
//
//                SearchScope searchScope = ApplicationManager.getApplication().runReadAction(new Computable<SearchScope>() {
//                    public SearchScope compute() {
//                        return queryParameters.getEffectiveSearchScope();
//                    }
//                });
//                if (searchScope instanceof GlobalSearchScope) {
//                    searchScope = GlobalSearchScope.getScopeRestrictedByFileTypes((GlobalSearchScope) searchScope, TwelfFileType.INSTANCE);
//                }
//                final TextOccurenceProcessor processor = new TextOccurenceProcessor() {
//                    public boolean execute(PsiElement element, int offsetInElement) {
//                        ProgressManager.checkCanceled();
//                        final PsiReference[] refs = element.getReferences();
//                        for (PsiReference ref : refs) {
//                            if (ReferenceRange.containsOffsetInElement(ref, offsetInElement) && ref.isReferenceTo(refElement)) {
//                                return consumer.process(ref);
//                            }
//                            ProgressManager.checkCanceled();
//                        }
//                        return true;
//                    }
//                };
//
//                if (!processElementsWithWord(processor, searchScope, name, UsageSearchContext.ANY, true)) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
//
//    public boolean processElementsWithWord(@NotNull final TextOccurenceProcessor processor,
//                                           @NotNull SearchScope searchScope,
//                                           @NotNull final String text,
//                                           short searchContext,
//                                           final boolean caseSensitively) {
//        if (text.length() == 0) {
//            throw new IllegalArgumentException("Cannot search for elements with empty text");
//        }
//        final ProgressIndicator progress = ProgressManager.getInstance().getProgressIndicator();
//        if (searchScope instanceof GlobalSearchScope) {
//            StringSearcher searcher = new StringSearcher(text, caseSensitively, true);
//
//            return processElementsWithTextInGlobalScope(processor,
//                    (GlobalSearchScope) searchScope,
//                    searcher,
//                    searchContext, caseSensitively);
//        } else {
//            LocalSearchScope scope = (LocalSearchScope) searchScope;
//            PsiElement[] scopeElements = scope.getScope();
//            final boolean ignoreInjectedPsi = scope.isIgnoreInjectedPsi();
//
//            Processor<PsiElement> processor1 = new Processor<PsiElement>() {
//                public boolean process(PsiElement scopeElement) {
//                    return processElementsWithWordInScopeElement(scopeElement, processor, text, caseSensitively, ignoreInjectedPsi, progress);
//                }
//            };
//            return JobUtil.invokeConcurrentlyUnderProgress(Arrays.asList(scopeElements), processor1, false, progress);
//        }
//    }
//
//    private static boolean processElementsWithWordInScopeElement(final PsiElement scopeElement,
//                                                                 final TextOccurenceProcessor processor,
//                                                                 final String word,
//                                                                 final boolean caseSensitive,
//                                                                 final boolean ignoreInjectedPsi, final ProgressIndicator progress) {
//        return ApplicationManager.getApplication().runReadAction(new Computable<Boolean>() {
//            public Boolean compute() {
//                StringSearcher searcher = new StringSearcher(word, caseSensitive, true);
//
//                return LowLevelSearchUtil.processElementsContainingWordInElement(processor, scopeElement, searcher, ignoreInjectedPsi, progress);
//            }
//        }).booleanValue();
//    }
//
//
//    private boolean processElementsWithTextInGlobalScope(@NotNull final TextOccurenceProcessor processor,
//                                                         @NotNull final GlobalSearchScope scope,
//                                                         @NotNull final StringSearcher searcher,
//                                                         final short searchContext,
//                                                         final boolean caseSensitively) {
//        LOG.assertTrue(!Thread.holdsLock(PsiLock.LOCK), "You must not run search from within updating PSI activity. Please consider invokeLatering it instead.");
//        final ProgressIndicator progress = ProgressManager.getInstance().getProgressIndicator();
//        if (progress != null) {
//            progress.pushState();
//            progress.setText(PsiBundle.message("psi.scanning.files.progress"));
//        }
//
//        String text = searcher.getPattern();
//        List<VirtualFile> fileSet = getFilesWithText(scope, searchContext, caseSensitively, text, progress);
//
//        if (progress != null) {
//            progress.setText(PsiBundle.message("psi.search.for.word.progress", text));
//        }
//
//        return processPsiFileRoots(progress, fileSet, new Processor<PsiElement>() {
//            public boolean process(PsiElement psiRoot) {
//                return LowLevelSearchUtil.processElementsContainingWordInElement(processor, psiRoot, searcher, false, progress);
//            }
//        });
//    }
}
