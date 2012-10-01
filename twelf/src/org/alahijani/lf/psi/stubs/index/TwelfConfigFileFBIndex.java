package org.alahijani.lf.psi.stubs.index;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.alahijani.lf.fileTypes.TwelfConfigFileType;
import org.alahijani.lf.psi.api.TwelfConfigFile;
import org.alahijani.lf.psi.api.TwelfFileReference;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <code>TwelfConfigFile</code>s indexed by Twelf file names they contain
 *
 * @author Ali Lahijani
 */
public class TwelfConfigFileFBIndex extends FileBasedIndexExtension<String, Integer> {

    public static final ID<String, Integer> KEY = ID.create("twelf.sources.cfg");

    private EnumeratorStringDescriptor myKeyDescriptor = new EnumeratorStringDescriptor();
    private FileBasedIndex.InputFilter myInputFilter = new FileBasedIndex.InputFilter() {
        public boolean acceptInput(VirtualFile file) {
            return file.getFileType() == TwelfConfigFileType.INSTANCE;
        }
    };
    private final DataExternalizer<Integer> myValueExternalizer = new DataExternalizer<Integer>() {
        public void save(final DataOutput out, final Integer value) throws IOException {
            out.writeInt(value);
        }

        public Integer read(final DataInput in) throws IOException {
            return in.readInt();
        }
    };
    private DataIndexer<String, Integer, FileContent> myDataIndexer = new DataIndexer<String, Integer, FileContent>() {
        @NotNull
        public Map<String, Integer> map(FileContent inputData) {
            TwelfFileReference[] memberFiles = ((TwelfConfigFile) inputData.getPsiFile()).getMemberFiles();

            HashMap<String, Integer> map = new HashMap<String, Integer>();
            for (int i = 0; i < memberFiles.length; i++) {
                TwelfFileReference memberFile = memberFiles[i];
                map.put(memberFile.getCanonicalText(), i);
            }
            return map;
        }
    };

    public static boolean getContainingConfigFiles(String elfFileName,
                                                   GlobalSearchScope scope, FileBasedIndex.ValueProcessor<Integer> processor) {
        return FileBasedIndex.getInstance().processValues(KEY, elfFileName, null, processor, scope);
    }

    @Override
    @NotNull
    public ID<String, Integer> getName() {
        return KEY;
    }

    @Override
    @NotNull
    public DataIndexer<String, Integer, FileContent> getIndexer() {
        return myDataIndexer;
    }

    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return myKeyDescriptor;
    }

    @Override
    public DataExternalizer<Integer> getValueExternalizer() {
        return myValueExternalizer;
    }

    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return myInputFilter;
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @Override
    public int getVersion() {
        return 0;
    }

}
