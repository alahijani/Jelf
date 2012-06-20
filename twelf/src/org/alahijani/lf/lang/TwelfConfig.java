package org.alahijani.lf.lang;

import com.intellij.lang.Language;

/**
 * @author Ali Lahijani
 */
public class TwelfConfig extends Language {

    public static final TwelfConfig INSTANCE = new TwelfConfig();

    public TwelfConfig() {
        super("TwelfConfig");
/*
        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory() {
            @NotNull
            protected SyntaxHighlighter createHighlighter() {
                return new TwelfSyntaxHighlighter();
            }
        });
*/
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }

}
