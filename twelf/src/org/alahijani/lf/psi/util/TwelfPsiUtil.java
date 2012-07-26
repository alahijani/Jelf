package org.alahijani.lf.psi.util;

import org.alahijani.lf.psi.api.TwelfBaseElement;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Ali Lahijani
 */
public class TwelfPsiUtil {

    public static Iterable<TwelfBaseElement> getVirtualParentChain(final TwelfBaseElement element) {
        return new Iterable<TwelfBaseElement>() {
            public Iterator<TwelfBaseElement> iterator() {
                return new Iterator<TwelfBaseElement>() {
                    TwelfBaseElement next = element;
                    public boolean hasNext() {
                        return next != null;
                    }

                    @NotNull
                    public TwelfBaseElement next() {
                        if (next == null)
                            throw new NoSuchElementException();

                        try {
                            return next;
                        } finally {
                            next = next.getVirtualParent();
                        }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

}
