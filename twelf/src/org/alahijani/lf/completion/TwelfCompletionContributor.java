package org.alahijani.lf.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.alahijani.lf.lang.TwelfTokenType;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.alahijani.lf.psi.api.LfIdentifierReference;
import org.alahijani.lf.psi.api.TwelfBaseElement;
import org.alahijani.lf.psi.api.TwelfIdentifier;
import org.alahijani.lf.psi.xref.Referencing;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

/**
 * @author Ali Lahijani
 */
public class TwelfCompletionContributor extends CompletionContributor {

    public TwelfCompletionContributor() {
        @SuppressWarnings({"unchecked"}) PsiElementPattern.Capture<PsiElement> idRef =
                psiElement(TwelfTokenType.IDENT).withParents(TwelfIdentifier.class, LfIdentifierReference.class);
        extend(CompletionType.BASIC, idRef, new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          ProcessingContext context,
                                          @NotNull CompletionResultSet result) {

                final PsiElement position = parameters.getPosition();
                final PsiElement reference = position.getParent();
                if (reference == null) return;
                if (reference instanceof TwelfBaseElement) {
                    Referencing.lookup(reference, result);
                }
            }
        });

        PsiElementPattern.Capture<PsiElement> directiveStart = psiElement(TwelfTokenType.DIRECTIVE);
        @SuppressWarnings({"unchecked"}) PsiElementPattern.Capture<PsiElement> globalStart =
                psiElement().withParents(TwelfIdentifier.class, LfGlobalVariable.class);
        @SuppressWarnings({"unchecked"}) ElementPattern<PsiElement> statementStart =
                or(globalStart, directiveStart);
        extend(CompletionType.BASIC, statementStart, new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          ProcessingContext context,
                                          @NotNull CompletionResultSet result) {

                if (parameters.getPosition().getNode().getElementType() == TwelfTokenType.DIRECTIVE) {
                    result = result.withPrefixMatcher("%" + result.getPrefixMatcher().getPrefix());
                }
                for (String directive : TwelfTokenType.DIRECTIVES) {
                    result.addElement(LookupElementBuilder.create(directive).setBold());
                }
            }
        });

        extend(CompletionType.BASIC, psiElement().afterLeaf("%infix"), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
                result.addElement(LookupElementBuilder.create("right").setBold());
                result.addElement(LookupElementBuilder.create("left").setBold());
                result.addElement(LookupElementBuilder.create("none").setBold());
            }
        });

        extend(CompletionType.BASIC, psiElement(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          ProcessingContext context,
                                          @NotNull CompletionResultSet result) {

                final PsiElement position = parameters.getPosition();
                String s = position.toString();
            }
        });


    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        context.setDummyIdentifier("_ ");    // it is sure not to resolve
    }
}
