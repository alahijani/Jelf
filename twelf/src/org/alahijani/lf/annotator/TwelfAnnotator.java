package org.alahijani.lf.annotator;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.editor.TwelfHighlighterColors;
import org.alahijani.lf.psi.api.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfAnnotator implements Annotator {

    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TwelfElement) {
            if (element instanceof LfDeclaration) {
                LfDeclaration declaration = (LfDeclaration) element;
                annotate(declaration.getNameIdentifier(), declaration, holder);
            }
            if (element instanceof LfIdentifierReference) {
                LfIdentifierReference reference = (LfIdentifierReference) element;
                PsiElement resolve = reference.resolve();
                annotate(reference.getIdentifier(), (LfDeclaration) resolve, holder);
            }
        }
    }

    private void annotate(LfIdentifier identifier, LfDeclaration declaration, AnnotationHolder holder) {
        if (declaration == null) {
            if (identifier.isAnonymous()) return;   // todo: this should be removed when we implement resolve() for anonymous identifiers a.k.a. term reconstruction

            holder.createErrorAnnotation(identifier, TwelfBundle.message("error.undeclared.identifier", identifier.getText()))
                    .setHighlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL);
        } else if (declaration instanceof LfMetaVariable) {
            holder.createInfoAnnotation(identifier, null).setTextAttributes(TwelfHighlighterColors.LF_META_VARIABLE);
        } else if (declaration instanceof LfGlobalVariable) {
            holder.createInfoAnnotation(identifier, null).setTextAttributes(TwelfHighlighterColors.LF_SIGNATURE_IDENTIFIER);
        } else {
            holder.createInfoAnnotation(identifier, null).setTextAttributes(TwelfHighlighterColors.LF_LOCAL_IDENTIFIER);
        }
    }

}
