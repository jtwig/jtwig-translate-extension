package org.jtwig.translate.message.source.localized.provider.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import static org.jtwig.translate.message.source.localized.provider.io.AndFileFilter.and;
import static org.jtwig.translate.message.source.localized.provider.io.NotDirectoryFileFilter.notDirectory;

public class DirectoryFileFinder implements FileFinder {
    private static final DirectoryFileFinder INSTANCE = new DirectoryFileFinder();

    public static DirectoryFileFinder directory() {
        return INSTANCE;
    }

    private DirectoryFileFinder() {}

    public Collection<File> find (File baseDirectory, FileFilter fileFilter) {
        Collection<File> result = new ArrayList<>();
        if (baseDirectory.exists()) {
            File[] files = baseDirectory.listFiles(and(notDirectory(), fileFilter));
            for (File file : files) {
                result.add(file);
            }
        }
        return result;
    }
}
