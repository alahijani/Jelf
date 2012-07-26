package org.alahijani.lf.compiler;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import org.alahijani.lf.fileTypes.TwelfFileType;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Note: Current versions of Twelf always use UTF-8 in their output.
 * todo: even if source files' encodings are something else?
 *
 * @author Ali Lahijani
 */
public class TwelfOutputParser implements Runnable {
    private static final Logger LOG = Logger.getInstance("#org.alahijani.lf.compiler.TwelfOutputParser");

    private final CompileContext context;
    private InputStream inputStream;
    private int tabSize;

    public TwelfOutputParser(Project project, CompileContext context, InputStream inputStream) {
        this.context = context;
        this.inputStream = inputStream;
        this.tabSize = CodeStyleSettingsManager.getSettings(project).getTabSize(TwelfFileType.INSTANCE);
        if (tabSize <= 0) {
            tabSize = 1;
        }
    }

    private static final Pattern ERROR_PATTERN = Pattern.compile("(\\w:)?([^:]+):" +
            "(\\d+)\\.(\\d+)-" +
            "(\\d+)\\.(\\d+)" +
            "\\s*Error:.*");

    private static final Pattern INFO_PATTERN = Pattern.compile("\\[(Opening|Closing) file (\\w:)?([^:]+)\\]");

    private static final Pattern EXCEPTION_PATTERN = Pattern.compile("Uncaught exception: (.*)");

    private void parseOutput() throws IOException {
        LineNumberReader in = new LineNumberReader(new InputStreamReader(inputStream, "UTF-8"));

        try {
            String error = null;
            String url = null;
            int lineNum = -1;
            int columnNum = -1;

            for (String line = in.readLine(); line != null; line = in.readLine()) {

                context.getProgressIndicator().checkCanceled();

                Matcher m = ERROR_PATTERN.matcher(line);
                if (m.matches()) {
                    if (error != null) {
                        addMessage(CompilerMessageCategory.ERROR, error, url, lineNum, columnNum);
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
                    error = "";                 // start error mode
                    continue;
                }

                m = INFO_PATTERN.matcher(line);
                if (m.matches()) {
                    if ("Closing".equals(m.group(1))) {
                        /**
                         * cannot set to null or to the latest .cfg file because Twelf first declares Closing a
                         * file and then proceeds to report errors there. May be we should keep two url variables
                         * TODO: when a filename in a .cfg file cannot be opened we currently report the error at the
                         * previous file
                         */
                        // url = null;
                        context.getProgressIndicator().popState();
                    } else if ("Opening".equals(m.group(1))) {
                        String drive = m.group(2);
                        String filePath = m.group(3);
                        url = getURL(drive, filePath);
                        context.getProgressIndicator().pushState();
                        context.getProgressIndicator().setText(line);
                    }
                    continue;
                }

                m = EXCEPTION_PATTERN.matcher(line);
                if (m.matches()) {
                    error = m.group(1) + "\n";
                    continue;
                }

                if ("%% ABORT %%".equals(line)) {
                    if (error != null) {
                        addMessage(CompilerMessageCategory.ERROR, error, url, lineNum, columnNum);
                    } else {
                        addMessage(CompilerMessageCategory.ERROR, "Compilation aborted", url, -1, -1);
                    }
                    error = null;       // end error mode
                    continue;
                }

                if (error != null) {     // we are after an error, so collect everything
                    error = error + line + "\n";
                } else {
                    // making the like unique by adding line numbers prevents Intellij from dropping "duplicate" lines
                    String message = "{%" + in.getLineNumber() + "%}    " + line;
                    addMessage(CompilerMessageCategory.INFORMATION, message, url, -1, -1);

                    context.getProgressIndicator().setText2(line);
                    // if (line.indexOf('.') != -1) parsedStatement();
                }
            }

            if (error != null) {
                addMessage(CompilerMessageCategory.ERROR, error, url, lineNum, columnNum);
            }
        } finally {
            in.close();
        }
    }

//    private int parsedStatements = 0;
//    private void parsedStatement() {
//        parsedStatements++;
//        context.getProgressIndicator().setFraction(parsedStatements / (double) ....);
//    }

    private static String getURL(String drive, String filePath) {
        if (drive != null && !drive.isEmpty()) {
            filePath = drive + filePath;
        }
        return VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, filePath);
    }

    private void addMessage(CompilerMessageCategory category,
                            String message, String url, int lineNum, int columnNum) {
        try {
            if (lineNum > 0 && url != null) {
                VirtualFile file = VirtualFileManager.getInstance().findFileByUrl(url);
                Document document = file == null ? null : FileDocumentManager.getInstance().getDocument(file);
                if (document != null) {
                    String lineText = getLineText(lineNum, document);
                    columnNum--;    // make it zero-based
                    columnNum = adjustForUTF8(lineText, columnNum);
                    columnNum = adjustForTabs(lineText, columnNum);
                    columnNum++;    // back to one-based
                }
            }
            context.addMessage(category, message, url, lineNum, columnNum);
        } catch (Exception e) {
            LOG.warn(e);
        }
    }

    private String getLineText(int lineNum, Document document) {
        int docLine = lineNum - 1;
        int startOffset = document.getLineStartOffset(docLine);
        int endOffset = document.getLineEndOffset(docLine);
        return document.getText().substring(startOffset, endOffset);
    }

    public int adjustForTabs(String text, int offset) {
        /**
         * Sometimes Twelf reports errors after the line end, maybe because of some presumed EOF or \0
         * character at the end
         */
        offset = Math.min(offset, text.length());
        return EditorUtil.calcColumnNumber(null, text, 0, offset, tabSize);
    }


    /**
     * @param text      string indexes point into
     * @param utf8Index zero-based index of UTF-8 bytes
     * @return zero-based index of normal Java UTF-16 characters
     */
    private int adjustForUTF8(String text, int utf8Index) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (os.size() >= utf8Index) {
                    return i;
                }
                writer.append(chars[i]);
                writer.flush();
            }
            return utf8Index;
        } catch (IOException e) {
            return utf8Index;
        }
    }

    public void run() {
        try {
            parseOutput();
        } catch (IOException e) {
            TwelfServer.LOG.error(e);
        }
    }
}
