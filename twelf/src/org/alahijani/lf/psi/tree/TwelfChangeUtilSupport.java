package org.alahijani.lf.psi.tree;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.TreeCopyHandler;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.lang.TwelfElementType;
import org.alahijani.lf.psi.api.ReferableElement;
import org.alahijani.lf.psi.api.TwelfIdentifierReference;

import java.util.Map;

public class TwelfChangeUtilSupport implements TreeCopyHandler {

    public TreeElement decodeInformation(TreeElement element, final Map<Object, Object> decodingState) {
        if (element instanceof CompositeElement) {
            if (element.getElementType() == TwelfElementType.REFERENCE_EXPRESSION) {
                TwelfIdentifierReference ref = (TwelfIdentifierReference) SourceTreeToPsiMap.treeElementToPsi(element);
                final ReferableElement refMember = element.getCopyableUserData(REFERENCED_MEMBER_KEY);
                if (refMember != null) {
                    element.putCopyableUserData(REFERENCED_MEMBER_KEY, null);
                    PsiElement refElement = ref.resolve();
                    if (!refMember.getManager().areElementsEquivalent(refMember, refElement)) {
                        try {
                            // can restore only if short (otherwise qualifier should be already restored)
                            ref = (TwelfIdentifierReference) ref.bindToElement(refMember);
                        } catch (IncorrectOperationException ignored) {
                        }
                        return (TreeElement) SourceTreeToPsiMap.psiElementToTree(ref);
                    }
                }
                return element;
            }
        }
        return null;
    }

    public void encodeInformation(final TreeElement element, final ASTNode original, final Map<Object, Object> encodingState) {
        if (original instanceof CompositeElement) {
            if (original.getElementType() == TwelfElementType.REFERENCE_EXPRESSION) {
                ReferableElement result = ((TwelfIdentifierReference) original.getPsi()).resolve();
                if (result != null) {
                    element.putCopyableUserData(REFERENCED_MEMBER_KEY, result);
                }
            }
        }
    }

    private static final Key<ReferableElement> REFERENCED_MEMBER_KEY = Key.create("REFERENCED_MEMBER_KEY");
/*
    public TreeElement generateTreeFor(PsiElement original, CharTable table, PsiManager manager) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
*/

}
