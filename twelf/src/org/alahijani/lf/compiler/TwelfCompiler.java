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
import org.alahijani.lf.fileTypes.TwelfConfigFileType;
import org.alahijani.lf.fileTypes.TwelfFileType;
import org.alahijani.lf.settings.CompilerSettings;
import org.alahijani.lf.settings.TwelfPluginSettings;
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
        return TwelfFileType.INSTANCE.equals(file.getFileType()) || TwelfConfigFileType.INSTANCE.equals(file.getFileType());
    }

    public void compile(CompileContext context, Chunk<Module> moduleChunk, VirtualFile[] files, OutputSink sink) {
        compile(context, files);
    }

    private void compile(CompileContext context, VirtualFile[] files) {
        try {
            ArrayList<File> tempFiles = new ArrayList<File>();
            final TwelfServer twelfServer = TwelfServer.createTwelfServer(project, context);
            try {

                for (int i = 0, filesLength = files.length; i < filesLength; i++) {
                    VirtualFile file = files[i];

                    configureForFile(twelfServer, file);
//                    context.getProgressIndicator().pushState();
                    context.getProgressIndicator().setFraction(i / (double) filesLength);

                    if (TwelfConfigFileType.INSTANCE.equals(file.getFileType())) {
                        twelfServer.make(file);
                    } else if (TwelfFileType.INSTANCE.equals(file.getFileType())) {
                        singleTwelfFile(twelfServer, file, tempFiles);
                    }

//                    context.getProgressIndicator().popState();
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
            LOG.warn(e);
        }
    }

    private void configureForFile(TwelfServer twelfServer, VirtualFile file) throws IOException {
        CompilerSettings settings = TwelfPluginSettings.getCompilerSettings(file);
        twelfServer.setAutoFreeze(settings.isAutoFreeze());
        twelfServer.setUnsafe(settings.isUnsafe());
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
