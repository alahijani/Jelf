package org.alahijani.lf.compiler;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
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
 * @author Ali Lahijani
 */
public class TwelfOutputParser implements Runnable {
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
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String error = null;
            String url = null;
            int lineNum = -1;
            int columnNum = -1;

            for (String line = in.readLine(); line != null; line = in.readLine()) {

                Matcher m = ERROR_PATTERN.matcher(line);
                if (m.matches()) {
                    if (error != null) {
                        addMessage(context, CompilerMessageCategory.ERROR, error, url, lineNum, columnNum);
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
                    String drive = m.group(2);
                    String filePath = m.group(3);
                    url = getURL(drive, filePath);
                    // context.addMessage(CompilerMessageCategory.STATISTICS, line, url, -1, -1);
                    continue;
                }

                m = EXCEPTION_PATTERN.matcher(line);
                if (m.matches()) {
                    error = m.group(1) + "\n";
                    continue;
                }

                if ("%% ABORT %%".equals(line)) {
                    if (error != null) {
                        addMessage(context, CompilerMessageCategory.ERROR, error, url, lineNum, columnNum);
                    } else {
                        addMessage(context, CompilerMessageCategory.ERROR, "Compilation aborted", url, -1, -1);
                    }
                    error = null;       // end error mode
                    continue;
                }

                if (error != null) {     // we are after an error, so collect everything
                    error = error + line + "\n";
                }
            }

            if (error != null) {
                addMessage(context, CompilerMessageCategory.ERROR, error, url, lineNum, columnNum);
            }
        } finally {
            in.close();
        }
    }

    private static String getURL(String drive, String filePath) {
        if (drive != null && !drive.isEmpty()) {
            filePath = drive + filePath;
        }
        return VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, filePath);
    }

    private void addMessage(CompileContext context, CompilerMessageCategory category,
                            String message, String url, int lineNum, int columnNum) {
        if (lineNum > 0) {
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
        text += " ";
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
            OutputStreamWriter writer = new OutputStreamWriter(os);
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
