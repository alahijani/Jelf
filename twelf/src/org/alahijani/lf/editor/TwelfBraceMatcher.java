package org.alahijani.lf.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.TwelfTokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ali Lahijani
 */
public class TwelfBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] PAIRS = {
        new BracePair(TwelfTokenType.LPARENTH, TwelfTokenType.RPARENTH, false),
        new BracePair(TwelfTokenType.LBRACKET, TwelfTokenType.RBRACKET, false),
        new BracePair(TwelfTokenType.LBRACE, TwelfTokenType.RBRACE, true),

        // new BracePair(TwelfTokenType.STRING_BEGIN, TwelfTokenType.STRING_END, false),
    };

    public BracePair[] getPairs() {
      return PAIRS;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType ibraceType,
                                                   @Nullable IElementType tokenType) {
      return true;
    }

    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
      return openingBraceOffset;
    }
}
