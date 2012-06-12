package org.alahijani.lf.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.xref.Referencing;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * @author Ali Lahijani
 */
public class TwelfCompletionContributor extends CompletionContributor {

/*
    private static final ElementPattern<PsiElement> AFTER_NEW =
            psiElement().afterLeaf(psiElement()
                    .withText(PsiKeyword.NEW)
                    .andNot(psiElement().afterLeaf(psiElement().withText(PsiKeyword.THROW))))
                    .withSuperParent(3, TwelfVariable.class);
*/

    private static final ElementPattern<PsiElement> AFTER_COLON = psiElement().afterLeaf(":").withParent(LfDeclaration.class);

    private static final ElementPattern<PsiElement> AFTER_TERM = psiElement().afterSibling(psiElement(LfTerm.class));

    public TwelfCompletionContributor() {
        extend(CompletionType.BASIC, psiElement(PsiElement.class), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          ProcessingContext context,
                                          @NotNull final CompletionResultSet result) {

                final PsiElement position = parameters.getPosition();
                final PsiElement reference = position.getParent();
                if (reference == null) return;
                if (reference instanceof TwelfElement) {
                    Referencing.lookup(reference, result);
                }
            }
        });

        extend(CompletionType.BASIC, AFTER_COLON, new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                System.out.println("parameters = " + parameters);

            }
        });

        extend(CompletionType.BASIC, AFTER_TERM, new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                System.out.println("parameters = " + parameters);
/*
                final LfIdentifierReference refExpr = ((LfIdentifierReference) parameters.getPosition().getParent());
                if (refExpr.getQualifier() != null) return;
                final TwelfArgumentList argumentList = (TwelfArgumentList) refExpr.getParent();
                final TwelfCall call = (TwelfCall) argumentList.getParent();
                List<TwelfResolveResult> results = new ArrayList<TwelfResolveResult>();
                //costructor call
                if (call instanceof TwelfConstructorCall) {
                    TwelfConstructorCall constructorCall = (TwelfConstructorCall) call;
                    results.addAll(Arrays.asList(constructorCall.multiResolveConstructor()));
                    results.addAll(Arrays.asList(constructorCall.multiResolveClass()));
                } else if (call instanceof ApplicationExpression) {
                    ApplicationExpression constructorCall = (ApplicationExpression) call;
                    results.addAll(Arrays.asList(constructorCall.getMethodVariants()));
                    final PsiType type = ((ApplicationExpression) call).getType();
                    if (type instanceof PsiClassType) {
                        final PsiClass psiClass = ((PsiClassType) type).resolve();
                        results.add(new TwelfResolveResultImpl(psiClass, true));
                    }
                } else if (call instanceof TwelfApplicationStatement) {
                    final TwelfExpression element = ((TwelfApplicationStatement) call).getFunExpression();
                    if (element instanceof TwelfReferenceElement) {
                        results.addAll(Arrays.asList(((TwelfReferenceElement) element).multiResolve(true)));
                    }
                }

                Set<PsiClass> usedClasses = new HashSet<PsiClass>();
                Set<String> usedNames = new HashSet<String>();
                for (TwelfNamedArgument argument : argumentList.getNamedArguments()) {
                    final TwelfArgumentLabel label = argument.getLabel();
                    if (label != null) {
                        final String name = label.getName();
                        if (name != null) {
                            usedNames.add(name);
                        }
                    }
                }

                for (TwelfResolveResult resolveResult : results) {
                    PsiElement element = resolveResult.getElement();
                    if (element instanceof PsiMethod) {
                        final PsiMethod method = (PsiMethod) element;
                        final PsiClass containingClass = method.getContainingClass();
                        if (containingClass != null) {
                            addPropertiesForClass(result, usedClasses, usedNames, containingClass, call);
                        }
                        if (method instanceof TwelfMethod) {
                            Set<String>[] parametersArray = ((TwelfMethod) method).getNamedParametersArray();
                            for (Set<String> namedParameters : parametersArray) {
                                for (String parameter : namedParameters) {
                                    final LookupElementBuilder lookup =
                                            LookupElementBuilder.create(parameter).setIcon(TwelfIcons.DYNAMIC).setInsertHandler(new NamedArgumentInsertHandler());
                                    result.addElement(lookup);
                                }
                            }
                        }
                    } else if (element instanceof PsiClass) {
                        addPropertiesForClass(result, usedClasses, usedNames, (PsiClass) element, call);
                    }
                }
*/
            }
        });
    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        context.setDummyIdentifier("_ ");    // it is sure not to resolve
    }
}
