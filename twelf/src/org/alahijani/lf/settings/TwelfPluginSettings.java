package org.alahijani.lf.settings;

import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Ali Lahijani
 */
public class TwelfPluginSettings {

    private static final String TWELF_INSTALLATION_OPTION = "twelf.installation";

    public static final String TWELF_SERVER = "\\Program Files\\Twelf\\bin\\twelf-server.bat";

    public static String getTwelfServer() {
        return TWELF_SERVER;
    }

    public static String[] getDummyIdentifiers() {
        return new String[] {"-"};
    }

    public static CompilerSettings getCompilerSettings(VirtualFile file) {
        return new CompilerSettings(false, false);
    }
}
