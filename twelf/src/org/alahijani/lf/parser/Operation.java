package org.alahijani.lf.parser;

import com.intellij.psi.tree.IElementType;
import org.alahijani.lf.TwelfElementType;

/**
 * @author Ali Lahijani
 */
public class Operation {
    public enum Associativity {
        Left, Right,
    }


    public static class Operator extends Operation {
        private int precedence;
        private IElementType elementType;

        public int getPrecedence() {
            return precedence;
        }

        public Operator(int precedence, IElementType elementType) {
            this.precedence = precedence;
            this.elementType = elementType;
        }

        public IElementType getElementType() {
            return elementType;
        }
    }

    public static class Infix extends Operator {
        private Associativity associativity;

        public Infix(int precedence, Associativity associativity, IElementType elementType) {
            super(precedence, elementType);
            this.associativity = associativity;
        }

        public Associativity getAssociativity() {
            return associativity;
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

    public static class Atom extends Operation {
    }

    public static final Infix juxOp = new Infix(Integer.MAX_VALUE, Associativity.Left, TwelfElementType.APPLICATION);
    public static final Infix arrowOp = new Infix(0, Associativity.Right, TwelfElementType.TO_ARROW_TERM);
    public static final Infix backArrowOp = new Infix(0, Associativity.Left, TwelfElementType.FROM_ARROW_TERM);
    public static final Infix colonOp = new Infix(0, Associativity.Left, TwelfElementType.TYPED_TERM);

}
