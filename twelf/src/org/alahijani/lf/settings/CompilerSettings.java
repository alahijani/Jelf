package org.alahijani.lf.settings;

/**
 * @author Ali Lahijani
 */
public class CompilerSettings {
    private boolean autoFreeze;
    private boolean unsafe;

    public CompilerSettings(boolean autoFreeze, boolean unsafe) {
        this.autoFreeze = autoFreeze;
        this.unsafe = unsafe;
    }

    public boolean isAutoFreeze() {
        return autoFreeze;
    }

    public boolean isUnsafe() {
        return unsafe;
    }
}
