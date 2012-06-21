package org.alahijani.lf.psi.stubs.elements;

import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.*;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.util.io.StringRef;
import org.alahijani.lf.lang.TwelfConfig;
import org.alahijani.lf.psi.api.TwelfConfigFile;
import org.alahijani.lf.psi.stubs.TwelfConfigFileStub;
import org.alahijani.lf.psi.stubs.impl.TwelfConfigFileStubImpl;
import org.alahijani.lf.psi.stubs.index.TwelfConfigFileIndex;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Ali Lahijani
 */
public class TwelfConfigStubFileElementType extends IStubFileElementType<TwelfConfigFileStub> {

    public TwelfConfigStubFileElementType() {
        super("Twelf config file", TwelfConfig.INSTANCE);
    }

    public String getExternalId() {
        return "twelfConfig.FILE";
    }

    /**
     * @return a value I guess we have to increase whenever format of <code>twelfConfig.FILE</code> changes and we
     *         want to require an index rebuild
     */
    public int getStubVersion() {
        return 2;
    }

    public StubBuilder getBuilder() {
        return new DefaultStubBuilder() {
            protected StubElement createStubForFile(final PsiFile file) {
                if (file instanceof TwelfConfigFile) {
                    return new TwelfConfigFileStubImpl((TwelfConfigFile) file);
                }

                return super.createStubForFile(file);
            }
        };
    }

    @Override
    public void indexStub(PsiFileStub stub, IndexSink sink) {
        super.indexStub(stub, sink);
    }

    public void indexStub(final TwelfConfigFileStub stub, final IndexSink sink) {
        ArrayList<StringRef> elfList = stub.getElfList();
        for (StringRef elf : elfList) {
            sink.occurrence(TwelfConfigFileIndex.KEY, elf.getString());
        }
    }

    @Override
    public void serialize(TwelfConfigFileStub stub, StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName().getString());
        ArrayList<StringRef> elfList = stub.getElfList();
        dataStream.writeInt(elfList.size());
        for (StringRef elf : elfList) {
            dataStream.writeName(elf.getString());
        }
    }

    @Override
    public TwelfConfigFileStub deserialize(StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef name = dataStream.readName();
        int num = dataStream.readInt();
        ArrayList<StringRef> list = new ArrayList<StringRef>(num);
        for (int i = num; i > 0; i--) {
            list.add(dataStream.readName());
        }
        return new TwelfConfigFileStubImpl(name, list);
    }
}
