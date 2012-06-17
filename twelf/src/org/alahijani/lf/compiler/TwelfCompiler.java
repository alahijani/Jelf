package org.alahijani.lf.compiler;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Chunk;
import org.alahijani.lf.TwelfBundle;
import org.alahijani.lf.fileTypes.TwelfFileType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @author Ali Lahijani
 */
public class TwelfCompiler implements TranslatingCompiler {
    private static final Logger LOG = Logger.getInstance("#org.alahijani.lf.compiler.TwelfCompiler");

    private static final String TWELF_INSTALLATION_OPTION = "twelf.installation";
    private Project project;

    public TwelfCompiler(Project project) {
        this.project = project;
    }

    @NotNull
    public String getDescription() {
        return TwelfBundle.message("twelf.compiler.description");
    }

    public boolean validateConfiguration(CompileScope scope) {
        return true;
    }


    public boolean isCompilableFile(VirtualFile file, CompileContext context) {
        return TwelfFileType.TWELF_FILE_TYPE.equals(file.getFileType());
    }

    public void compile(CompileContext context, Chunk<Module> moduleChunk, VirtualFile[] files, OutputSink sink) {
        compile(context, files, true, false);
    }

    private void compile(CompileContext context, VirtualFile[] files, boolean autoFreeze, boolean unsafe) {
        try {
            ArrayList<File> tempFiles = new ArrayList<File>();
            final TwelfServer twelfServer = TwelfServer.createTwelfServer(context);
            try {
                twelfServer.setAutoFreeze(autoFreeze);
                twelfServer.setUnsafe(unsafe);

                for (VirtualFile file : files) {
                    singleTwelfFile(twelfServer, file, tempFiles);

                    twelfServer.reset();
                }

                twelfServer.waitFor();
            } finally {
                twelfServer.destroy();
                for (File temp : tempFiles) {
                    boolean deleted = temp.delete();
                    LOG.assertTrue(deleted);
                }
            }
        } catch (InterruptedException ignored) {
            LOG.info("Thread interrupted", ignored);
        } catch (ExecutionException ignored) {
            LOG.info("Thread interrupted", ignored);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    private void singleTwelfFile(TwelfServer twelfServer, VirtualFile twelf, ArrayList<File> tempFiles) throws IOException {
        File cfg = File.createTempFile("sources", ".cfg", new File(twelf.getPath()).getParentFile());
        try {
            printName(twelf, cfg);
            twelfServer.make(cfg);
        } finally {
            tempFiles.add(cfg);
        }
    }

    private void printName(VirtualFile twelf, File cfg) throws IOException {
        FileWriter writer = new FileWriter(cfg);
        try {
            writer.append(twelf.getName());
            writer.append("\n");
        } finally {
            writer.close();
        }
    }

}
