package org.alahijani.lf.compiler;

import com.intellij.openapi.compiler.*;
import com.intellij.openapi.vfs.VirtualFile;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.fileTypes.TwelfFileType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.IOException;

/**
 * @author Ali Lahijani
 */
public class TwelfCompiler implements Validator {

    private static final boolean SOURCE_ONLY = false;
    private static final String TWELF_INSTALLATION_OPTION = "twelf.installation";

    @NotNull
    public ProcessingItem[] getProcessingItems(CompileContext context) {
        CompileScope scope = context.getCompileScope();
        VirtualFile[] files = scope.getFiles(TwelfFileType.TWELF_FILE_TYPE, SOURCE_ONLY);
        ProcessingItem[] items = new ProcessingItem[files.length];
        for (int i = 0; i < files.length; i++) {
            items[i] = new MyProcessingItem(files[i]);
        }
        return items;
    }

    public ProcessingItem[] process(CompileContext context, ProcessingItem[] items) {
        // todo: actually compile
        return new ProcessingItem[0];
    }

    @NotNull
    public String getDescription() {
        return TwelfBundle.message("twelf.compiler.description");
    }

    public boolean validateConfiguration(CompileScope scope) {
        /*
        Module[] modules = scope.getAffectedModules();
        for (Module module : modules) {
            String installation = module.getOptionValue(TWELF_INSTALLATION_OPTION);
            if (installation == null) {
                return false;
            }
        }
        */
        return true;
    }

    public ValidityState createValidityState(DataInput in) throws IOException {
        return new EmptyValidityState();
    }

    private static class MyProcessingItem implements ProcessingItem {
        private final VirtualFile file;

        public MyProcessingItem(VirtualFile file) {
            this.file = file;
        }

        @NotNull
        public VirtualFile getFile() {
            return file;
        }

        public ValidityState getValidityState() {
            return null; // does not depend on anything else
        }
    }
}
