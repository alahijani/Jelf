package org.alahijani.lf.params;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.xref.LfLookupItem;
import org.alahijani.lf.formatter.TwelfFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


/**
 * @author Ali Lahijani
 */
public class TwelfParameterInfoHandler implements ParameterInfoHandler/*WithTabActionSupport*/<ApplicationExpression, LfDeclaration> {

    // --------- General stuff --------------------------------------

    public String getParameterCloseChars() {
        // todo: do we really need this?
        return "){}";
    }

    public boolean tracksParameterIndex() {
        return false;   // you track it for me
    }

    public boolean couldShowInLookup() {
        return true;
    }

    // --------- ApplicationExpression stuff ------------------------

    @Nullable
    private static ApplicationExpression findAnchorElement(int offset, PsiFile file) {
        PsiElement element = file.findElementAt(offset);
        if (element == null) return null;

        ApplicationExpression app = PsiTreeUtil.getParentOfType(element, ApplicationExpression.class);
        return expand(app);
    }

    private static ApplicationExpression expand(ApplicationExpression app) {
        if (app == null) {
            return null;
        }

        do {
            PsiElement par = app.getParent();
            if (!(par instanceof ApplicationExpression)) {
                return app;
            }
            if (((ApplicationExpression) par).getFunction() != app) {
                return app;
            }
            app = (ApplicationExpression) par;
        } while (true);
    }

    public ApplicationExpression findElementForParameterInfo(CreateParameterInfoContext context) {
        return findAnchorElement(context.getEditor().getCaretModel().getOffset(), context.getFile());
    }

    public void showParameterInfo(@NotNull ApplicationExpression place, CreateParameterInfoContext context) {
        CurriedApplication curry = CurriedApplication.curry(place);
        if (curry == null) return;
        PsiElement resolve = curry.head.resolve();
        context.setItemsToShow(new Object[]{resolve});
        context.showHint(curry.application, curry.application.getTextRange().getStartOffset(), this);
    }

    public ApplicationExpression findElementForUpdatingParameterInfo(UpdateParameterInfoContext context) {
        ApplicationExpression application = findAnchorElement(context.getEditor().getCaretModel().getOffset(), context.getFile());
        if (application == null) {
            return null;
        }
        if (PsiTreeUtil.isAncestor(context.getParameterOwner(), application, false)) {
            // we are showing parameters of f for "f x (g y)" and the caret enters the scope of g.
            // we should continue with f
            application = (ApplicationExpression) context.getParameterOwner();
        }
        return application;
    }

    public void updateParameterInfo(@NotNull ApplicationExpression place, UpdateParameterInfoContext context) {
        CurriedApplication curry = CurriedApplication.curry(place);
        if (curry == null) return;
        if (!PsiTreeUtil.isAncestor(context.getParameterOwner(), curry.application, false)) {
            // we are showing parameters of g for "f x (g y)" and the caret enters the scope of f.
            // we should stop with g
            context.removeHint();
        }

        int offset = context.getEditor().getCaretModel().getOffset();
        int currIndex = curry.getCurrentParameterIndex(offset);
        context.setCurrentParameter(currIndex);

    }

    // --------- LfDeclaration stuff --------------------------------

    public void updateUI(LfDeclaration function, ParameterInfoUIContext context) {

        if (function == null || !function.isValid()) {
            context.setUIComponentEnabled(false);
            return;
        }

        StringBuffer buffer = new StringBuffer();

        // curry Pi types
        LfTerm type = function.getType();
        ArrayList<LfLocalVariable> params = new ArrayList<LfLocalVariable>();
        while (type instanceof PiType) {
            PiType pi = (PiType) type;
            params.add(pi.getBoundDeclaration());
            type = pi.getReturnType();
        }

        buffer.append(function.getName());
        buffer.append(": ");

        int highlightStartOffset = -1;
        int highlightEndOffset = -1;

        final int current = context.getCurrentParameterIndex();
        int size = params.size();

        // final PsiSubstitutor substitutor = function.getSubstitutor();
        for (int j = 0; j < size; j++) {
            LfLocalVariable param = params.get(j);

            int startOffset = buffer.length();
            appendParameterText(param, /*substitutor,*/ buffer);
            int endOffset = buffer.length();

            if (context.isUIComponentEnabled() && j == current) {
                highlightStartOffset = startOffset;
                highlightEndOffset = endOffset;
            }
        }

        // append the return type
        buffer.append("\t");
        buffer.append(TwelfFormatter.format(type));

        final boolean isDeprecated = false;
        context.setupUIComponentPresentation(
                buffer.toString(),
                highlightStartOffset,
                highlightEndOffset,
                !context.isUIComponentEnabled(),
                isDeprecated,
                false,
                context.getDefaultParameterColor()
        );
    }

    private void appendParameterText(LfLocalVariable param, StringBuffer buffer) {
        // if (param.isAnonymous() && param.getType() != null) {
        //     buffer.append(param.getType().getText());
        //     buffer.append(" -> ");
        // } else {
        buffer.append("{");
        buffer.append(param.getName());

        LfTerm type = param.getType();
        while (type instanceof WrappingTerm) {
            type = ((WrappingTerm) type).getWrapped();
        }
        if (type != null) {
            buffer.append(": ");
            buffer.append(type.getText());
        }

        buffer.append("} ");
        // }
    }

    public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context) {
        if (item instanceof LfLookupItem) {
            ReferableElement target = ((LfLookupItem) item).getDeclaration();
            if (target instanceof LfDeclaration) {
                return getParams(((LfDeclaration) target).getType());
            }
        }

        return null;
    }

    public Object[] getParametersForDocumentation(LfDeclaration function, ParameterInfoContext context) {
        return getParams(function.getType());
    }

    private LfLocalVariable[] getParams(LfTerm type) {
        ArrayList<LfLocalVariable> params = new ArrayList<LfLocalVariable>();

        while (type instanceof PiType) {
            PiType pi = (PiType) type;
            params.add(pi.getBoundDeclaration());
            type = pi.getReturnType();
        }
        return params.toArray(new LfLocalVariable[params.size()]);
    }

}
