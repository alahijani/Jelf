package org.alahijani.lf.compiler;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ali Lahijani
 */
public class TwelfServer {
    private static final String TWELF_SERVER = "\\Program Files\\Twelf\\bin\\twelf-server.bat";
    private Process process;
    private Writer out;

    private TwelfServer(Process process) {
        this.process = process;
        out = new OutputStreamWriter(process.getOutputStream());
    }

    private static String getTwelfServer() {
        return TWELF_SERVER;
    }

    public static TwelfServer createTwelfServer() throws IOException {
        Process process = Runtime.getRuntime().exec(getTwelfServer());
        return new TwelfServer(process);
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
        out.append("make ").append(cfg.getPath().replace(File.separatorChar, '/'));
    }

    public int waitFor() throws InterruptedException, IOException {
        out.close();    // twelf expects an EOF
        return process.waitFor();
    }

    public void destroy() {
        process.destroy();
    }

    private static Pattern ERROR_PATTERN = Pattern.compile("(\\w:)?([^:]+):" +
            "(\\d+)\\.(\\d+)-" +
            "(\\d+)\\.(\\d+)" +
            "\\s*Error:.*");

    private static Pattern INFO_PATTERN = Pattern.compile("\\[(Opening|Closing) file (\\w:)?([^:]+)\\]");

    public void parseErrors(CompileContext context) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

        try {
            String line = in.readLine();

            String url = null;
            String error = null;
            Integer lineNum = null;
            Integer columnNum = null;
            do {
                Matcher m = ERROR_PATTERN.matcher(line);
                if (m.matches()) {
                    if (error != null) {
                        context.addMessage(CompilerMessageCategory.ERROR, error, url, lineNum, columnNum);
                    }

                    String drive = m.group(1);
                    String filePath = m.group(2);
                    String startLine = m.group(3);
                    String startColumn = m.group(4);
                    String endLine = m.group(5);
                    String endColumn = m.group(6);

                    url = getURL(drive, filePath);
                    lineNum = Integer.valueOf(startLine);
                    columnNum = Integer.valueOf(startColumn);
                    error = "";                 // start a new error
                } else {
                    m = INFO_PATTERN.matcher(line);
                    if (m.matches()) {
                        // String drive = m.group(2);
                        // String filePath = m.group(3);
                        // context.addMessage(CompilerMessageCategory.STATISTICS, line, getURL(drive, filePath), -1, -1);
                    } else if (error != null) {     // we are after an error, so collect everything
                        error += line + "\n";
                    }
                }
                line = in.readLine();
            } while (line != null);

            if (error != null) {
                context.addMessage(CompilerMessageCategory.ERROR, error, url, lineNum, columnNum);
            }
        } finally {
            in.close();
        }
    }

    private String getURL(String drive, String filePath) {
        if (drive != null && !drive.isEmpty()) {
            filePath = drive + filePath;
        }
        return "file://" + filePath;
    }
}
