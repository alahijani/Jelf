package org.alahijani.lf.formatter;

import org.alahijani.lf.psi.api.LfTerm;

/**
 * @author Ali Lahijani
 */
public class TwelfFormatter {
    public static String format(LfTerm term) {
        return term == null ? "< unknown >" : format(term.getText());
    }

    private static String format(String text) {
        text = text.replace('\t', ' ');
        text = text.replace('\n', ' ');
        text = text.replace('\r', ' ');
        text = text.replace('\u000B', ' ');
        text = text.replace('\u000C', ' ');

        while (text.contains("  ")) {
            text = text.replace("  ", " ");
        }
        return text;
    }
}
