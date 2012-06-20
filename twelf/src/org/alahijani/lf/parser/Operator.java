package org.alahijani.lf.parser;

import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.lang.TwelfElementType;

/**
 * @author Ali Lahijani
 */
public class Operator {
    public enum Associativity {
        Left, Right,
    }

    public final int precedence;
    public final IElementType elementType;

    public Operator(int precedence, IElementType elementType) {
        this.precedence = precedence;
        this.elementType = elementType;
    }

    public static class Infix extends Operator {
        public final Associativity associativity;

        public Infix(int precedence, Associativity associativity, IElementType elementType) {
            super(precedence, elementType);
            this.associativity = associativity;
        }
    }

    public static class Prefix extends Operator {
        public Prefix(int precedence, IElementType elementType) {
            super(precedence, elementType);
        }
    }

    public static class Postfix extends Operator {
        public Postfix(int precedence, IElementType elementType) {
            super(precedence, elementType);
        }
    }

    public static final Infix juxOp = new Infix(Integer.MAX_VALUE, Associativity.Left, TwelfElementType.APPLICATION);
    public static final Infix arrowOp = new Infix(0, Associativity.Right, TwelfElementType.TO_ARROW_TERM);
    public static final Infix backArrowOp = new Infix(0, Associativity.Left, TwelfElementType.FROM_ARROW_TERM);
    public static final Infix colonOp = new Infix(0, Associativity.Left, TwelfElementType.TYPED_TERM);

}
