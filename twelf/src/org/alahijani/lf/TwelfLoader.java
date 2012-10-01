package org.alahijani.lf;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfLoader implements ApplicationComponent {

    public void initComponent() {
//        TwelfEditorActionsManager.registerTwelfEditorActions();

//        ChangeUtil.registerCopyHandler(new TwelfChangeUtilSupport());
//        ChangeUtil.registerTreeGenerator(new TwelfTreeGenerator());

        //Register Keyword completion
//        setupCompletion();

//        ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter() {
//            public void projectOpened(final Project project) {
//                final TextEditorHighlightingPassRegistrar registrar = TextEditorHighlightingPassRegistrar.getInstance(project);
//                TwelfUnusedImportsPassFactory unusedImportsPassFactory = project.getComponent(TwelfUnusedImportsPassFactory.class);
//                registrar.registerTextEditorHighlightingPass(unusedImportsPassFactory, new int[]{Pass.UPDATE_ALL}, null, true, -1);
//
//                DebuggerManager.getInstance(project).registerPositionManagerFactory(new Function<DebugProcess, PositionManager>() {
//                    public PositionManager fun(DebugProcess debugProcess) {
//                        return new TwelfPositionManager(debugProcess);
//                    }
//                });
//
//            }
//        });
    }

//    private static void setupCompletion() {
//        CompositeCompletionData compositeCompletionData = new CompositeCompletionData(new TwelfCompletionData(), new TwelfDocCompletionData());
//        CompletionUtil.registerCompletionData(TwelfFileType.INSTANCE, compositeCompletionData);
//    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "twelf.support.loader";
    }

}
