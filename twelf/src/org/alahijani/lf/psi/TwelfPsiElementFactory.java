package org.alahijani.lf.psi;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import org.alahijani.lf.lexer.IdentifierTokenParser;
import org.alahijani.lf.psi.api.LfIdentifier;
import org.alahijani.lf.psi.light.LightIdentifier;

public class TwelfPsiElementFactory {
    private final PsiManager myManager;

    public TwelfPsiElementFactory(PsiManager _manager) {
        myManager = _manager;
    }

    public static TwelfPsiElementFactory getInstance(Project project) {
        return ServiceManager.getService(project, TwelfPsiElementFactory.class);
    }

    public LfIdentifier createIdentifier(String text) {
        IdentifierTokenParser.checkIsIdentifier(text);
        return new LightIdentifier(myManager, text);
    }

/*
    private TwelfFile createDummyFile(String s, boolean isPhysical) {
        return (GroovyFileImpl) PsiFileFactory.getInstance(myProject).createFileFromText("DUMMY__." + GroovyFileType.GROOVY_FILE_TYPE.getDefaultExtension(), GroovyFileType.GROOVY_FILE_TYPE, s, System.currentTimeMillis(), isPhysical);
    }
*/
}