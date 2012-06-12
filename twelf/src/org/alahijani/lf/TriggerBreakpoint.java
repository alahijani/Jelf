package org.alahijani.lf;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;

/**
 * @author Ali Lahijani
 */
public class TriggerBreakpoint extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        int i = 12;
        e.getDataContext().getData(DataConstants.PSI_FILE);
//        com.intellij.openapi.fileTypes.impl.AbstractFileType@16772}
    }
}
