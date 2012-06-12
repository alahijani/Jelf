package org.alahijani.lf.psi.xref;

import com.intellij.psi.AbstractElementManipulator;
import org.alahijani.lf.psi.api.LfIdentifierReference;

/**
 * @author Ali Lahijani
 */
public abstract class LfIdentifierReferenceManipulator extends AbstractElementManipulator<LfIdentifierReference> {

//    public LfIdentifierReference handleContentChange(LfIdentifierReference ref, TextRange range, String newContent)
//            throws IncorrectOperationException {
//
//        final StringBuilder replacement = new StringBuilder(ref.getCanonicalText());
//        final int valueOffset = ref.getTextRange().getStartOffset() - ref.getTextOffset();
//
//        replacement.replace(
//                range.getStartOffset() - valueOffset,
//                range.getEndOffset() - valueOffset,
//                newContent
//        );
//        ref.getValue().setText(replacement.toString());
//        return ref;
//    }
//
//    public TextRange getRangeInElement(final LfIdentifierReference tag) {
//        if (tag.getSubTags().length > 0) {
//            // Text range in tag with subtags is not supported, return empty range, consider making this function nullable.
//            return new TextRange(0, 0);
//        }
//
//        final LfIdentifierReferenceValue value = tag.getValue();
//        final XmlText[] texts = value.getTextElements();
//        switch (texts.length) {
//            case 0:
//                return value.getTextRange().shiftRight(-tag.getTextOffset());
//            case 1:
//                return getValueRange(texts[0]);
//            default:
//                return new TextRange(0, 0);
//        }
//    }
//
//    private static TextRange getValueRange(final XmlText xmlText) {
//        final int offset = xmlText.getStartOffsetInParent();
//        final String value = xmlText.getValue();
//        final String trimmed = value.trim();
//        final int i = value.indexOf(trimmed);
//        final int start = xmlText.displayToPhysical(i) + offset;
//        return trimmed.length() == 0 ? new TextRange(start, start) : new TextRange(start, xmlText.displayToPhysical(i + trimmed.length() - 1) + offset + 1);
//    }
//
//    public static TextRange[] getValueRanges(final @NotNull LfIdentifierReference tag) {
//        final LfIdentifierReferenceValue value = tag.getValue();
//        final XmlText[] texts = value.getTextElements();
//        if (texts.length == 0) {
//            return new TextRange[]{value.getTextRange().shiftRight(-tag.getTextOffset())};
//        } else {
//            final TextRange[] ranges = new TextRange[texts.length];
//            for (int i = 0; i < texts.length; i++) {
//                ranges[i] = getValueRange(texts[i]);
//            }
//            return ranges;
//        }
//    }
}
