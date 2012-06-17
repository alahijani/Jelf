package org.alahijani.lf.compiler;

import com.intellij.openapi.application.ApplicationManager;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        for (VirtualFile file : files) {
            compile(context, moduleChunk, file, sink);
        }
    }

    private void compile(CompileContext context, Chunk<Module> moduleChunk, VirtualFile file, OutputSink sink) {
        System.out.println("file = " + file);
        try {
            File cfg = File.createTempFile("sources", ".cfg", new File(file.getPath()).getParentFile());
            try {
                FileWriter writer = new FileWriter(cfg);
                try {
                    writer.append(file.getName());
                    writer.append("\n");
                } finally {
                    writer.close();
                }

                boolean autoFreeze = false;
                boolean unsafe = true;

                final TwelfServer twelfServer = TwelfServer.createTwelfServer();
                try {
                    twelfServer.setAutoFreeze(autoFreeze);
                    twelfServer.setUnsafe(unsafe);
                    twelfServer.make(cfg);
                    Future<?> future = parseErrors(twelfServer, context);
                    twelfServer.waitFor();
                    joinThread(future);
                } catch (InterruptedException e) {
                    // ignore
                } finally {
                    twelfServer.destroy();
                }

            } finally {
                boolean deleted = cfg.delete();
                LOG.assertTrue(deleted);
            }
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    private Future<?> parseErrors(final TwelfServer twelfServer, final CompileContext context) {
        return ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            public void run() {
                try {
                    twelfServer.parseErrors(context);
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        });
    }

    private void joinThread(final Future<?> threadFuture) {
        if (threadFuture != null) {
            try {
                threadFuture.get();
            } catch (InterruptedException ignored) {
                LOG.info("Thread interrupted", ignored);
            } catch (ExecutionException ignored) {
                LOG.info("Thread interrupted", ignored);
            }
        }
    }
}
