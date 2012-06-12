package org.alahijani.lf.editor;

import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.TwelfTokenType;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ali Lahijani
 */
public class TwelfCommenter implements CodeDocumentationAwareCommenter {
    public String getLineCommentPrefix() {
        return "% ";        // todo : cannot uncomment "%%"
    }

    public String getBlockCommentPrefix() {
        return "%{";
    }

    public String getBlockCommentSuffix() {
        return "}%";
    }

    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    public String getCommentedBlockCommentSuffix() {
        return null;
    }

    @Nullable
    public IElementType getLineCommentTokenType() {
        return TwelfTokenType.END_OF_LINE_COMMENT;
    }

    @Nullable
    public IElementType getBlockCommentTokenType() {
        return TwelfTokenType.BLOCK_COMMENT;
    }

    @Nullable
    public IElementType getDocumentationCommentTokenType() {
        return TwelfTokenType.DOC_COMMENT;
    }

    @Nullable
    public String getDocumentationCommentPrefix() {
        // todo: check with Literate Twelf
        return "%{";
    }

    @Nullable
    public String getDocumentationCommentLinePrefix() {
        return "";
    }

    @Nullable
    public String getDocumentationCommentSuffix() {
        return "}%";
    }

    public boolean isDocumentationComment(PsiComment element) {
        return element.getText().startsWith(getDocumentationCommentPrefix());
    }
}

