package org.jtwig.translate.message.source.localized.provider.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

public interface FileFinder {
    Collection<File> find (File baseDirectory, FileFilter fileFilter);
}
