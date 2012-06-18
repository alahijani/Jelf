package org.alahijani.lf.compiler;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Ali Lahijani
 */
public class TwelfServer {
    static final Logger LOG = Logger.getInstance("#org.alahijani.lf.compiler.TwelfServer");

    private static final String TWELF_SERVER = "\\Program Files\\Twelf\\bin\\twelf-server.bat";
    private Process process;
    private Future<?> future;
    private Writer out;

    private TwelfServer(Project project, Process process, CompileContext context) {
        this.process = process;
        this.out = new OutputStreamWriter(process.getOutputStream());
        TwelfOutputParser outputParser = new TwelfOutputParser(project, context, process.getInputStream());
        this.future = ApplicationManager.getApplication().executeOnPooledThread(outputParser);
    }

    private static String getTwelfServer() {
        return TWELF_SERVER;
    }

    public static TwelfServer createTwelfServer(Project project, CompileContext context) throws IOException {
        Process process = Runtime.getRuntime().exec(getTwelfServer());
        return new TwelfServer(project, process, context);
    }

    public void setAutoFreeze(boolean autoFreeze) throws IOException {
        setProperty("autoFreeze", autoFreeze);
    }

    public void setUnsafe(boolean unsafe) throws IOException {
        setProperty("unsafe", unsafe);
    }

    private void setProperty(String property, Object value) throws IOException {
        out.append("set ").append(property).append(" ").append(String.valueOf(value)).append("\n");
    }

    public void make(File cfg) throws IOException {
        make(cfg.getPath());
    }

    public void make(VirtualFile cfg) throws IOException {
        make(cfg.getPath());
    }

    private void make(String path) throws IOException {
        out.append("make ").append(path.replace(File.separatorChar, '/')).append("\n");
    }

    public int waitFor() throws InterruptedException, IOException, ExecutionException {
        out.close();    // Twelf expects an EOF
        future.get();
        return process.waitFor();
    }

    public void reset() throws IOException {
        out.append("reset\n");
    }

    public void destroy() {
        process.destroy();
    }
}

