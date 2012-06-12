package org.alahijani.lf.lexer;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import org.alahijani.lf.TwelfTokenType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ali Lahijani
 */
public class TwelfNamesValidator implements NamesValidator {
    private List<String> keywords = Arrays.asList(TwelfTokenType.KEYWORDS);

    public boolean isKeyword(String name, Project project) {
        return keywords.contains(name);
    }

    public boolean isIdentifier(String name, Project project) {
        return IdentifierTokenParser.isIdentifier(name);
    }

}
