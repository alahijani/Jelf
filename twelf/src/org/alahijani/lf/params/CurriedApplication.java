package org.alahijani.lf.params;

import org.alahijani.lf.psi.api.ApplicationExpression;
import org.alahijani.lf.psi.api.LfIdentifierReference;
import org.alahijani.lf.psi.api.LfTerm;

import java.util.LinkedList;

/**
 * @author Ali Lahijani
 */
public class CurriedApplication /*extends LightElement implements PsiElement*/ {

    public final ApplicationExpression application;
    public final LfIdentifierReference head;
    public final LfTerm[] arguments;

    public CurriedApplication(ApplicationExpression application, LfIdentifierReference head, LfTerm[] arguments) {
        // super(application.getManager(), Twelf.INSTANCE);
        this.application = application;
        this.head = head;
        this.arguments = arguments;
    }

    public int getCurrentParameterIndex(int offset) {
        int idx = -1;
        for (LfTerm argument : arguments) {
            if (argument.getTextRange().getStartOffset() > offset) break;
            idx++;
        }
        return idx;
    }

    public static CurriedApplication curry(ApplicationExpression application) {
        LinkedList<LfTerm> args = new LinkedList<LfTerm>();
        ApplicationExpression app = application;
        do {
            LfTerm function = app.getFunction();
            LfTerm argument = app.getArgument();
            args.add(0, argument);
            if (function instanceof LfIdentifierReference) {
                return new CurriedApplication(
                        application,
                        (LfIdentifierReference) function,
                        args.toArray(new LfTerm[args.size()])
                );
            } else if (function instanceof ApplicationExpression) {
                app = (ApplicationExpression) function;
            } else {
                return null;
            }
        } while (true);
    }

    @Override
    public String toString() {
        return application.toString();
    }
}
