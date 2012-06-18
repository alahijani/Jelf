package org.alahijani.lf;

import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.util.PsiTreeUtil;
import org.alahijani.lf.compiler.TwelfCompiler;
import org.alahijani.lf.fileTypes.TwelfConfigFileType;
import org.alahijani.lf.fileTypes.TwelfFileType;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.LfIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfProject implements ProjectComponent {
    private Project myProject;
    private PsiTreeChangeAdapter treeChangeAdapter;

    public TwelfProject(Project project) {
        myProject = project;
        treeChangeAdapter = new PsiTreeChangeAdapter() {
            @Override
            public void childReplaced(PsiTreeChangeEvent event) {
                LfIdentifier identifier = PsiTreeUtil.getParentOfType(event.getNewChild(), LfIdentifier.class);
                if (identifier == null) {
                    return;
                }

                PsiElement parent = identifier.getParent();
                if (!(parent instanceof LfDeclaration)) {
                    return;
                }

                ((LfDeclaration) parent).getResolveScope();
            }
        };

//        PsiManager.getInstance(myProject).addPsiTreeChangeListener(treeChangeAdapter);

    }

    public void projectOpened() {
        CompilerManager compilerManager = CompilerManager.getInstance(myProject);
        compilerManager.addCompilableFileType(TwelfFileType.INSTANCE);
        compilerManager.addCompilableFileType(TwelfConfigFileType.INSTANCE);
        compilerManager.addCompiler(new TwelfCompiler(myProject));
    }

    public void projectClosed() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
//        PsiManager.getInstance(myProject).removePsiTreeChangeListener(treeChangeAdapter);
    }

    @NotNull
    public String getComponentName() {
        return "twelf.project.component";
    }
}
