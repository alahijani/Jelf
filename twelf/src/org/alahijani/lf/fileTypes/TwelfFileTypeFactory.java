package org.alahijani.lf.fileTypes;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ali Lahijani
 */
public class TwelfFileTypeFactory extends com.intellij.openapi.fileTypes.FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        TwelfFileType twelf = TwelfFileType.TWELF_FILE_TYPE;
        fileTypeConsumer.consume(twelf, "elf");
        fileTypeConsumer.consume(twelf, "lf");
        fileTypeConsumer.consume(twelf, "llf");
        fileTypeConsumer.consume(twelf, "quy");
//        fileTypeConsumer.consume(twelf, "lf");
    }

}
