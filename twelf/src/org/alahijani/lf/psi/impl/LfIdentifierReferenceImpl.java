package org.alahijani.lf.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import org.alahijani.lf.psi.TwelfElementVisitor;
import org.alahijani.lf.psi.api.LfDeclaration;
import org.alahijani.lf.psi.api.LfIdentifier;
import org.alahijani.lf.psi.api.LfIdentifierReference;
import org.alahijani.lf.psi.xref.Referencing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ali Lahijani
 */
public class LfIdentifierReferenceImpl extends TwelfElementImpl implements LfIdentifierReference {

    public LfIdentifierReferenceImpl(@NotNull ASTNode node) {
        super(node);
    }

    public PsiReference getReference() {
        return this;
    }

    public PsiElement getElement() {
        return this;
    }

    @Nullable
    public PsiElement getReferenceNameElement() {
        return getIdentifier();
    }

    public LfIdentifier getIdentifier() {
        return findChildByClass(LfIdentifier.class);
    }

    public TextRange getRangeInElement() {
        final PsiElement refNameElement = getReferenceNameElement();
        if (refNameElement != null) {
            final int offsetInParent = refNameElement.getStartOffsetInParent();
            return new TextRange(offsetInParent, offsetInParent + refNameElement.getTextLength());
        }
        return new TextRange(0, getTextLength());
    }

    @NotNull
    public String getCanonicalText() {
        return getRangeInElement().substring(getElement().getText());
    }

    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        if (isReferenceTo(element)) return this;

      if (element instanceof LfDeclaration) {
          handleElementRename(((LfDeclaration) element).getName());
      }

        throw new IncorrectOperationException("Cannot bind to:" + element + " of class " + element.getClass());
    }

    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        Referencing.rename(getIdentifier(), newElementName);
        return this;
    }

    /*
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        ElementManipulator<LfIdentifierReferenceImpl> manipulator = ElementManipulators.getManipulator(this);
        assert manipulator != null: "Cannot find manipulator for " + this;
        return manipulator.handleContentChange(this, getRangeInElement(), newElementName);
    }
*/
/*
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        LfIdentifier myIdentifier = getIdentifier();
        if (myIdentifier != null) {
            LfIdentifier identifier = TwelfPsiElementFactory.getInstance(getProject()).createIdentifier(newElementName);
            myIdentifier.replace(identifier);
        }

        return this;
    }
*/

    public boolean isReferenceTo(PsiElement element) {
        if (element instanceof PsiNamedElement) {
            return getManager().areElementsEquivalent(element, correctSearchTargets(resolve()));
        }
        return false;
    }

    @Nullable
    public static PsiElement correctSearchTargets(@Nullable PsiElement target) {
        if (target != null && !target.isPhysical()) {
            return target.getNavigationElement();
        }
        return target;
    }

    @NotNull
    public Object[] getVariants() {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }


    public boolean isSoft() {
        return false;
    }

    public PsiElement resolve() {
        return Referencing.resolveTarget(this);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TwelfElementVisitor) {
            ((TwelfElementVisitor) visitor).visitReference(this);
        } else {
            visitor.visitElement(this);
        }
    }


    /*
    public PsiElement resolve() {
        ResolveResult[] results = getManager().getResolveCache().resolveWithCaching(this, RESOLVER, true, false);
        return results.length == 1 ? results[0].getElement() : null;
    }

    private static final ResolveCache.Resolver RESOLVER = new ResolveCache.Resolver() {
        public PsiElement resolve(PsiReference psiReference, boolean incompleteCode) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    };
*/


/*

    public String getReferenceName() {
        PsiElement nameElement = getReferenceNameElement();
        if (nameElement != null) {
            return nameElement.getText();
        }
        return null;
    }

    public PsiElement getQualifier() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    if implements PsiPolyVariantReference....

    @NotNull
    public ResolveResult[] multiResolve(boolean incomplete) {  //incomplete means we do not take arguments into consideration
        return getManager().getResolveCache().resolveWithCaching(this, RESOLVER, false, incomplete);
    }

    private static final TwelfResolver RESOLVER = new TwelfResolver();

    private static class TwelfResolver implements ResolveCache.PolyVariantResolver<LfIdentifierReferenceImpl> {
        public ResolveResult[] resolve(LfIdentifierReferenceImpl refExpr, boolean incompleteCode) {
            String name = refExpr.getReferenceName();
            if (name == null) return ResolveResult.EMPTY_ARRAY;

            if (incompleteCode) {
                ResolverProcessor processor = CompletionProcessor.createRefSameNameProcessor(refExpr, name);
                resolveImpl(refExpr, processor);
                ResolveResult[] propertyCandidates = processor.getCandidates();
                if (propertyCandidates.length > 0) return propertyCandidates;
            }

            switch (kind) {
                case METHOD_OR_PROPERTY:
                    return resolveMethodOrProperty(refExpr, name);
                case TYPE_OR_PROPERTY:
                    return resolveTypeOrProperty(refExpr, name);
                default:
                    return ResolveResult.EMPTY_ARRAY;
            }
        }

        private static ResolveResult[] resolveTypeOrProperty(LfIdentifierReferenceImpl refExpr, String name) {
            EnumSet<ClassHint.ResolveKind> kinds = refExpr.getParent() instanceof LfIdentifierReference
                    ? EnumSet.of(ClassHint.ResolveKind.CLASS, ClassHint.ResolveKind.PACKAGE)
                    : EnumSet.of(ClassHint.ResolveKind.CLASS);
            boolean hasAt = refExpr.hasAt();
            ResolveResult[] classCandidates = ResolveResult.EMPTY_ARRAY;
            if (!hasAt) {
                ResolverProcessor classProcessor = new ClassResolverProcessor(refExpr.getReferenceName(), refExpr, kinds);
                resolveImpl(refExpr, classProcessor);
                classCandidates = classProcessor.getCandidates();
                for (ResolveResult classCandidate : classCandidates) {
                    final PsiElement element = classCandidate.getElement();
                    if (element instanceof PsiClass && ((PsiClass) element).isEnum()) {
                        return classCandidates;
                    }
                }
            }

            ResolverProcessor processor = new PropertyResolverProcessor(name, refExpr);
            resolveImpl(refExpr, processor);
            final ResolveResult[] fieldCandidates = processor.getCandidates();

            if (refExpr.hasAt()) {
                return fieldCandidates;
            }

            //if reference expression is in class we need to return field instead of accessor method
            for (ResolveResult candidate : fieldCandidates) {
                final PsiElement element = candidate.getElement();
                if (element instanceof PsiField) {
                    final PsiClass containingClass = ((PsiField) element).getContainingClass();
                    if (containingClass != null && PsiTreeUtil.isAncestor(containingClass, refExpr, true))
                        return fieldCandidates;
                } else {
                    return fieldCandidates;
                }
            }

            final boolean isLValue = PsiUtil.isLValue(refExpr);
            String[] names;
            names = isLValue ? TwelfPropertyUtils.suggestSettersName(name) : TwelfPropertyUtils.suggestGettersName(name);
            List<ResolveResult> accessorResults = new ArrayList<ResolveResult>();
            for (String getterName : names) {
                AccessorResolverProcessor accessorResolver = new AccessorResolverProcessor(getterName, refExpr, !isLValue);
                resolveImpl(refExpr, accessorResolver);
                final ResolveResult[] candidates = accessorResolver.getCandidates(); //can be only one candidate
                if (candidates.length == 1 && candidates[0].isStaticsOK()) {
                    return candidates;
                } else {
                    accessorResults.addAll(Arrays.asList(candidates));
                }
            }
            if (fieldCandidates.length > 0) return fieldCandidates;
            if (accessorResults.size() > 0) return new ResolveResult[]{accessorResults.get(0)};

            return classCandidates;
        }

        private static ResolveResult[] resolveMethodOrProperty(LfIdentifierReferenceImpl refExpr, String name) {
            final PsiType[] argTypes = PsiUtil.getArgumentTypes(refExpr, false);
            PsiType thisType = getThisType(refExpr);

            MethodResolverProcessor methodResolver =
                    new MethodResolverProcessor(name, refExpr, false, thisType, argTypes, refExpr.getTypeArguments());
            resolveImpl(refExpr, methodResolver);
            if (methodResolver.hasApplicableCandidates()) return methodResolver.getCandidates();

            final String[] names = TwelfPropertyUtils.suggestGettersName(name);
            List<ResolveResult> list = new ArrayList<ResolveResult>();
            for (String getterName : names) {
                AccessorResolverProcessor getterResolver = new AccessorResolverProcessor(getterName, refExpr, true);
                resolveImpl(refExpr, getterResolver);
                final ResolveResult[] candidates = getterResolver.getCandidates(); //can be only one candidate
                if (candidates.length == 1 && candidates[0].isStaticsOK()) {
                    refExpr.putUserData(IS_RESOLVED_TO_GETTER, true);
                    return candidates;
                } else {
                    list.addAll(Arrays.asList(candidates));
                }
            }

            PropertyResolverProcessor propertyResolver = new PropertyResolverProcessor(name, refExpr);
            resolveImpl(refExpr, propertyResolver);
            if (propertyResolver.hasCandidates()) return propertyResolver.getCandidates();

            if (methodResolver.hasCandidates()) {
                return methodResolver.getCandidates();
            } else if (list.size() > 0) {
                refExpr.putUserData(IS_RESOLVED_TO_GETTER, true);
                return list.toArray(new ResolveResult[list.size()]);
            }

            return ResolveResult.EMPTY_ARRAY;
        }


        private static void resolveImpl(LfIdentifierReferenceImpl refExpr, ResolverProcessor processor) {
            LfTerm qualifier = refExpr.getQualifierExpression();
            if (qualifier == null) {
                ResolveUtil.treeWalkUp(refExpr, processor, true);
                if (!processor.hasCandidates()) {
                    qualifier = PsiImplUtil.getRuntimeQualifier(refExpr);
                    if (qualifier != null) {
                        processQualifier(refExpr, processor, qualifier);
                    }
                }
            } else {
                if (refExpr.getDotTokenType() != TwelfTokenType.mSPREAD_DOT) {
                    processQualifier(refExpr, processor, qualifier);
                } else {
                    processQualifierForSpreadDot(refExpr, processor, qualifier);
                }

                if (qualifier instanceof LfIdentifierReference && "class".equals(((LfIdentifierReference) qualifier).getReferenceName())) {
                    processIfJavaLangClass(refExpr, processor, qualifier.getType());
                } else if (qualifier instanceof GrThisReferenceExpression) {
                    processIfJavaLangClass(refExpr, processor, qualifier.getType());
                }
            }
        }

        private static void processIfJavaLangClass(LfIdentifierReferenceImpl refExpr, ResolverProcessor processor, PsiType type) {
            if (type instanceof PsiClassType) {
                final PsiClass psiClass = ((PsiClassType) type).resolve();
                if (psiClass != null && CommonClassNames.JAVA_LANG_CLASS.equals(psiClass.getQualifiedName())) {
                    final PsiType[] params = ((PsiClassType) type).getParameters();
                    if (params.length == 1) {
                        processClassQualifierType(refExpr, processor, params[0]);
                    }
                }
            }
        }

        private static void processQualifierForSpreadDot(LfIdentifierReferenceImpl refExpr, ResolverProcessor processor, LfTerm qualifier) {
            PsiType qualifierType = qualifier.getType();
            if (qualifierType instanceof PsiClassType) {
                PsiClassType.ClassResolveResult result = ((PsiClassType) qualifierType).resolveGenerics();
                PsiClass clazz = result.getElement();
                if (clazz != null) {
                    PsiClass listClass = ResolveUtil.findListClass(refExpr.getManager(), refExpr.getResolveScope());
                    if (listClass != null && listClass.getTypeParameters().length == 1) {
                        PsiSubstitutor substitutor = TypeConversionUtil.getClassSubstitutor(listClass, clazz, result.getSubstitutor());
                        if (substitutor != null) {
                            PsiType componentType = substitutor.substitute(listClass.getTypeParameters()[0]);
                            if (componentType != null) {
                                processClassQualifierType(refExpr, processor, componentType);
                            }
                        }
                    }
                }
            } else if (qualifierType instanceof PsiArrayType) {
                processClassQualifierType(refExpr, processor, ((PsiArrayType) qualifierType).getComponentType());
            }
        }

        private static void processQualifier(LfIdentifierReferenceImpl refExpr, ResolverProcessor processor, LfTerm qualifier) {
            PsiType qualifierType = qualifier.getType();
            if (qualifierType == null) {
                if (qualifier instanceof LfIdentifierReference) {
                    PsiElement resolved = ((LfIdentifierReference) qualifier).resolve();
                    if (resolved instanceof PsiPackage) {
                        if (!resolved.processDeclarations(processor, ResolveState.initial(), null, refExpr)) //noinspection UnnecessaryReturnStatement
                            return;
                    } else {
                        qualifierType = JavaPsiFacade.getInstance(refExpr.getProject()).getElementFactory()
                                .createTypeByFQClassName(CommonClassNames.JAVA_LANG_OBJECT, refExpr.getResolveScope());
                        processClassQualifierType(refExpr, processor, qualifierType);
                    }
                }
            } else {
                if (qualifierType instanceof PsiIntersectionType) {
                    for (PsiType conjunct : ((PsiIntersectionType) qualifierType).getConjuncts()) {
                        processClassQualifierType(refExpr, processor, conjunct);
                    }
                } else {
                    processClassQualifierType(refExpr, processor, qualifierType);
                    if (qualifier instanceof LfIdentifierReference) {
                        PsiElement resolved = ((LfIdentifierReference) qualifier).resolve();
                        if (resolved instanceof PsiClass) { //omitted .class
                            PsiClass javaLangClass = PsiUtil.getJavaLangClass(resolved, refExpr.getResolveScope());
                            if (javaLangClass != null) {
                                ResolveState state = ResolveState.initial();
                                PsiTypeParameter[] typeParameters = javaLangClass.getTypeParameters();
                                PsiSubstitutor substitutor = state.get(PsiSubstitutor.KEY);
                                if (substitutor == null) substitutor = PsiSubstitutor.EMPTY;
                                if (typeParameters.length == 1) {
                                    substitutor = substitutor.put(typeParameters[0], qualifierType);
                                    state = state.put(PsiSubstitutor.KEY, substitutor);
                                }
                                if (!javaLangClass.processDeclarations(processor, state, null, refExpr)) return;
                                PsiType javaLangClassType = JavaPsiFacade.getInstance(refExpr.getProject()).getElementFactory().createType(javaLangClass, substitutor);
                                ResolveUtil.processNonCodeMethods(javaLangClassType, processor, refExpr.getProject(), refExpr, false);
                            }
                        }
                    }
                }
            }
        }

        private static void processClassQualifierType(LfIdentifierReferenceImpl refExpr, ResolverProcessor processor, PsiType qualifierType) {
            Project project = refExpr.getProject();
            if (qualifierType instanceof PsiClassType) {
                PsiClassType.ClassResolveResult qualifierResult = ((PsiClassType) qualifierType).resolveGenerics();
                PsiClass qualifierClass = qualifierResult.getElement();
                if (qualifierClass != null) {
                    if (!qualifierClass.processDeclarations(processor,
                            ResolveState.initial().put(PsiSubstitutor.KEY, qualifierResult.getSubstitutor()), null, refExpr))
                        return;
                }
                if (!ResolveUtil.processCategoryMembers(refExpr, processor)) return;
            } else if (qualifierType instanceof PsiArrayType) {
                final GrTypeDefinition arrayClass = TwelfPsiManager.getInstance(project).getArrayClass();
                if (!arrayClass.processDeclarations(processor, ResolveState.initial(), null, refExpr)) return;
            } else if (qualifierType instanceof PsiIntersectionType) {
                for (PsiType conjunct : ((PsiIntersectionType) qualifierType).getConjuncts()) {
                    processClassQualifierType(refExpr, processor, conjunct);
                }
                return;
            }

            ResolveUtil.processNonCodeMethods(qualifierType, processor, project, refExpr, false);
        }
    }

*/

}
