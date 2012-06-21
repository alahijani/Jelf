package org.alahijani.lf.psi.stubs.impl;

import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.util.io.StringRef;
import org.alahijani.lf.lang.TwelfConfigParserDefinition;
import org.alahijani.lf.psi.api.TwelfConfigFile;
import org.alahijani.lf.psi.api.TwelfIdentifierReference;
import org.alahijani.lf.psi.stubs.TwelfConfigFileStub;

import java.util.ArrayList;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigFileStubImpl extends PsiFileStubImpl<TwelfConfigFile> implements TwelfConfigFileStub {
    private StringRef name;
    private ArrayList<StringRef> elfList;

    public TwelfConfigFileStubImpl(TwelfConfigFile file) {
        super(file);
        this.name = StringRef.fromString(file.getName());
        TwelfIdentifierReference[] memberFiles = file.getMemberFiles();
        this.elfList = new ArrayList<StringRef>(memberFiles.length);
        for (TwelfIdentifierReference elf : memberFiles) {
            this.elfList.add(StringRef.fromString(elf.getCanonicalText()));
        }
    }

    public TwelfConfigFileStubImpl(StringRef name, ArrayList<StringRef> elfList) {
        super(null);
        this.name = name;
        this.elfList = elfList;
    }

    public IStubFileElementType getType() {
        return TwelfConfigParserDefinition.TWELF_CONFIG_FILE;
    }

    public StringRef getName() {
        return name;
    }

    public ArrayList<StringRef> getElfList() {
        return elfList;
    }
}
