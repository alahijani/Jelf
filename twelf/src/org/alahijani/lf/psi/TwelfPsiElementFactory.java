package org.alahijani.lf.psi;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Query;
import org.alahijani.lf.fileTypes.TwelfFileType;
import org.alahijani.lf.lexer.IdentifierTokenParser;
import org.alahijani.lf.psi.api.*;
import org.alahijani.lf.psi.impl.TwelfFileImpl;
import org.alahijani.lf.psi.light.LightIdentifier;
import org.jetbrains.annotations.NotNull;

public class TwelfPsiElementFactory {
    private final PsiManager myManager;

    public TwelfPsiElementFactory(PsiManager _manager) {
        myManager = _manager;
    }

    public static TwelfPsiElementFactory getInstance(Project project) {
        return ServiceManager.getService(project, TwelfPsiElementFactory.class);
    }

    public TwelfIdentifier createIdentifier(TwelfElement virtualParent, String text) {
        IdentifierTokenParser.checkIsIdentifier(text);
        return new LightIdentifier(virtualParent, text);
    }

    public LfTerm createLfTerm(String text) {
        TwelfFileImpl file = (TwelfFileImpl) createTwelfFile("_ = " + text + ".");
        GlobalVariableBinder statement = (GlobalVariableBinder) file.getStatements()[0];
        return statement.getDeclaration().getValue();
    }

    public PsiFile createTwelfFile(String idText) {
        return createTwelfFile(idText, false);
    }

    public TwelfFile createTwelfFile(String idText, boolean isPhysical) {
        return createDummyFile(idText, isPhysical);
    }

    private TwelfFileImpl createDummyFile(String text, boolean isPhysical) {
        return (TwelfFileImpl) PsiFileFactory.getInstance(myManager.getProject()).createFileFromText("DUMMY__." +
                TwelfFileType.INSTANCE.getDefaultExtension(),
                TwelfFileType.INSTANCE,
                text,
                System.currentTimeMillis(), isPhysical);
    }

    public LfTerm inline(LfTerm reference, @NotNull LfTerm value) {
        while (value instanceof ParenthesizedExpression) {
            value = ((ParenthesizedExpression) value).getWrapped();
        }
        LfTerm newElement = createLfTerm("(" + value.getText() + ")");

        reference = (LfTerm) reference.replace(newElement);
        for (PsiElement e = reference.getParent(); e instanceof ApplicationExpression; e = e.getParent()) {
            e = inline((ApplicationExpression) e);
        }
        return reference;
    }

    public PsiElement inline(ApplicationExpression app) {
        LfTerm function = app.getFunction();
        LfTerm argument = app.getArgument();

        /**
         * unwrap it if wrapped
         */
        while (function instanceof WrappingTerm) {
            function = ((WrappingTerm) function).getWrapped();
        }

        if (function instanceof LambdaExpression) {
            LfTerm newElement = subst((LambdaExpression) function, argument);
            return captureAvoidingReplace(app, newElement);
        } else {
            return app;
        }
    }

    private PsiElement captureAvoidingReplace(LfTerm oldElement, LfTerm newElement) {
        newElement = avoidCapture(newElement, oldElement);
        return oldElement.replace(newElement);
    }

    private LfTerm avoidCapture(LfTerm beingMoved, LfTerm toPosition) {
        // TODO: rename meta-variables and other references in beingMoved if necessary
        return beingMoved;
    }

    public LfTerm subst(LocalVariableBinder binder, LfTerm argument) {
        LfTerm newElement = createLfTerm(binder.getBody().getText());

        final Query<PsiReference> query =
                ReferencesSearch.search(binder.getBoundDeclaration(), new LocalSearchScope(newElement));
        for (PsiReference reference : query.findAll()) {
            inline((LfTerm) reference.getElement(), argument);
        }

        return newElement;
    }

}