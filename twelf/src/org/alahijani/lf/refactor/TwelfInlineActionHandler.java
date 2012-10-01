package org.alahijani.lf.refactor;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.lang.refactoring.InlineHandler;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.refactoring.util.RefactoringMessageDialog;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.Query;
import com.intellij.util.containers.MultiMap;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.psi.TwelfPsiElementFactory;
import org.alahijani.lf.psi.api.LfGlobalVariable;
import org.alahijani.lf.psi.api.LfTerm;
import org.alahijani.lf.psi.api.TwelfStatement;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author Ali Lahijani
 */
public class TwelfInlineActionHandler implements InlineHandler {

    private String title = TwelfBundle.message("refactoring.inline.variable");

    public Settings prepareInlineElement(PsiElement element, Editor editor, boolean invokedOnReference) {
        if (!(element instanceof LfGlobalVariable)) {
            return null;
        }

        LfGlobalVariable variable = (LfGlobalVariable) element;
        final String name = variable.getName();
        final Project project = variable.getProject();
        LfTerm value = variable.getValue();

        if (value == null) {
            String message = RefactoringBundle.getCannotRefactorMessage(
                    RefactoringBundle.message("variable.has.no.initializer", name)
            );
            CommonRefactoringUtil.showErrorHint(project, editor, message, title, HelpID.INLINE_VARIABLE);
            return null;
        }

        final Query<PsiReference> query = ReferencesSearch.search(variable);
        if (query.findFirst() == null) {
            String message = RefactoringBundle.message("variable.is.never.used", name);
            CommonRefactoringUtil.showErrorHint(project, editor, message, title, HelpID.INLINE_VARIABLE);
            return null;
        }

        final Collection<PsiReference> refs = query.findAll();
        PsiReference[] toInline = refs.toArray(new PsiReference[refs.size()]);

        PsiFile workingFile = variable.getContainingFile();
        for (PsiReference reference : toInline) {
            final PsiFile otherFile = reference.getElement().getContainingFile();
            if (!otherFile.equals(workingFile)) {
                String message = RefactoringBundle.message("variable.is.referenced.in.multiple.files", name);
                CommonRefactoringUtil.showErrorHint(project, editor, message, title, HelpID.INLINE_VARIABLE);

                /*
                todo InlineFieldDialog dialog = new InlineFieldDialog(project, field, refExpression);
                dialog.show();
                */
                return null;
            }
        }

        final HighlightManager highlightManager = HighlightManager.getInstance(project);
        EditorColorsManager manager = EditorColorsManager.getInstance();
        final TextAttributes attributes = manager.getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
        highlightManager.addOccurrenceHighlights(
                editor,
                toInline,
                attributes, true, null
        );

        return inlineDialogResult(name, project, toInline);
    }

    public void removeDefinition(PsiElement element, Settings settings) {
        if (!(element instanceof LfGlobalVariable)) {
            return;
        }

        TwelfStatement parent = (TwelfStatement) element.getParent();
        parent.delete();
    }

    public Inliner createInliner(PsiElement element, Settings settings) {
        if (element instanceof LfGlobalVariable) {
            return new LfGlobalVariableInliner((LfGlobalVariable) element);
        }
        return null;
    }

    /**
     * Shows dialog with question to inline
     */
    @Nullable
    private InlineHandler.Settings inlineDialogResult(String name, Project project, PsiReference[] refs) {
        Application application = ApplicationManager.getApplication();
        if (!application.isUnitTestMode()) {
            int occurrencesCount = refs.length;
            String occurencesString = RefactoringBundle.message("occurences.string", occurrencesCount);
            final String question = RefactoringBundle.message("inline.local.variable.prompt", name)
                    + " " + occurencesString;
            RefactoringMessageDialog dialog = new RefactoringMessageDialog(
                    title,
                    question,
                    HelpID.INLINE_VARIABLE,
                    "OptionPane.questionIcon",
                    true,
                    project);
            dialog.show();
            if (!dialog.isOK()) {
                WindowManager.getInstance().getStatusBar(project).setInfo(RefactoringBundle.message("press.escape.to.remove.the.highlighting"));
                return null;
            }
        }

        return new InlineHandler.Settings() {
            public boolean isOnlyOneReferenceToInline() {
                return false;
            }
        };
    }

    private class LfGlobalVariableInliner implements Inliner {
        private LfGlobalVariable definition;

        public LfGlobalVariableInliner(LfGlobalVariable definition) {
            this.definition = definition;
        }

        public MultiMap<PsiElement, String> getConflicts(PsiReference reference, PsiElement referenced) {
            return MultiMap.EMPTY;
        }

        public void inlineUsage(UsageInfo usage, PsiElement referenced) {
            LfTerm reference = (LfTerm) usage.getElement();
            if (reference == null) return;

            LfTerm value = definition.getValue();
            assert value != null;

            Project project = definition.getProject();
            TwelfPsiElementFactory factory = TwelfPsiElementFactory.getInstance(project);

            reference = factory.inline(reference, value);

            final HighlightManager highlightManager = HighlightManager.getInstance(project);
            final EditorColorsManager manager = EditorColorsManager.getInstance();
            final TextAttributes attributes = manager.getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
            final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            assert editor != null;
            highlightManager.addOccurrenceHighlights(
                    editor,
                    new PsiElement[]{reference},
                    attributes, true, null
            );
        }

    }

}
