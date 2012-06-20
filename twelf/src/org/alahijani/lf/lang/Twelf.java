package org.alahijani.lf.lang;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class Twelf extends Language {

    public static final Twelf INSTANCE = new Twelf();

    public Twelf() {
        super(new LF(), "Twelf");
        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory() {
            @NotNull
            protected SyntaxHighlighter createHighlighter() {
                return new TwelfSyntaxHighlighter();
            }
        });
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }

}
