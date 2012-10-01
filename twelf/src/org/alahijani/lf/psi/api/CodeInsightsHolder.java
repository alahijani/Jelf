package org.alahijani.lf.psi.api;

/**
* @author Ali Lahijani
*/
public interface CodeInsightsHolder {
    public void hidesOtherDeclaration(LfGlobalVariable variable);

    public void hidesOtherDeclaration(LfGlobalVariable variable, TwelfConfigFile linkedVia);
}
