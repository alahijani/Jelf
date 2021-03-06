package org.alahijani.lf.annotator;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.editor.TwelfHighlighterColors;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.impl.IgnoredDirectiveImpl;
import org.alahijani.lf.psi.xref.Referencing;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfAnnotator implements Annotator {

    public void annotate(@NotNull PsiElement element, @NotNull final AnnotationHolder holder) {
        if (element instanceof TwelfBaseElement) {
            if (element instanceof LfDeclaration) {
                LfDeclaration declaration = (LfDeclaration) element;
                annotate(declaration.getNameIdentifier(), declaration, holder);
            }
            if (element instanceof LfIdentifierReference) {
                LfIdentifierReference reference = (LfIdentifierReference) element;
                Referencing.multiResolveTarget(reference, new MyCodeInsightsHolder(holder));
            }
            if (element instanceof TwelfIdentifierReference) {
                TwelfIdentifierReference reference = (TwelfIdentifierReference) element;
                ReferableElement resolve = reference.resolve();
                annotate(reference.getIdentifier(), resolve, holder);
            }
            if (element instanceof IgnoredDirectiveImpl) {
                String message = TwelfBundle.message("warning.directive.ignored", ((TwelfDirective) element).getDirectiveName());
                holder.createWarningAnnotation(element, message);
            }
        }
    }

    private void annotate(TwelfIdentifier identifier, ReferableElement target, AnnotationHolder holder) {
        if (target == null) {
            if (identifier.isAnonymous())
                return;   // todo: this should be removed when we implement resolve() for anonymous identifiers a.k.a. term reconstruction

            holder.createErrorAnnotation(identifier, TwelfBundle.message("error.cannot.resolve", identifier.getText()))
                    .setHighlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL);
        } else if (target instanceof LfMetaVariable) {
            holder.createInfoAnnotation(identifier, null).setTextAttributes(TwelfHighlighterColors.LF_META_VARIABLE);
        } else if (target instanceof LfGlobalVariable) {
            holder.createInfoAnnotation(identifier, null).setTextAttributes(TwelfHighlighterColors.LF_SIGNATURE_IDENTIFIER);
        } else if (target instanceof LfLocalVariable) {
            holder.createInfoAnnotation(identifier, null).setTextAttributes(TwelfHighlighterColors.LF_LOCAL_IDENTIFIER);
        } else if (target instanceof TwelfFile) {
            holder.createInfoAnnotation(identifier, null).setTextAttributes(TwelfHighlighterColors.TWELF_FILE);
        }
    }

    private static class MyCodeInsightsHolder implements CodeInsightsHolder {
        private final AnnotationHolder holder;

        public MyCodeInsightsHolder(AnnotationHolder holder) {
            this.holder = holder;
        }

        public void hidesOtherDeclaration(LfGlobalVariable variable) {
            holder.createWarningAnnotation(variable, TwelfBundle.message("warning.hides.other.declaration", variable.getName()))
                    .setHighlightType(ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
        }

        public void hidesOtherDeclaration(LfGlobalVariable variable, TwelfConfigFile linkedVia) {
            // todo not allowed
            holder.createWarningAnnotation(variable,
                    TwelfBundle.message("warning.hides.other.declaration.linked", variable.getName(), linkedVia.getName()))
                    .setHighlightType(ProblemHighlightType.GENERIC_ERROR_OR_WARNING);

        }
    }

}
