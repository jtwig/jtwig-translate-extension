package org.jtwig.translate.message.source.localized.provider.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import static org.jtwig.translate.message.source.localized.provider.io.AndFileFilter.and;
import static org.jtwig.translate.message.source.localized.provider.io.DirectoryFileFilter.isDirectory;
import static org.jtwig.translate.message.source.localized.provider.io.NotDirectoryFileFilter.notDirectory;

public class RecursiveFileFinder implements FileFinder {
    private static final RecursiveFileFinder INSTANCE = new RecursiveFileFinder();

    public static RecursiveFileFinder recursiveDirectory () {
        return INSTANCE;
    }

    private RecursiveFileFinder() {}

    @Override
    public Collection<File> find(File baseDirectory, FileFilter fileFilter) {
        Collection<File> result = new ArrayList<>();

        if (baseDirectory.exists()) {
            File[] files = baseDirectory.listFiles(and(notDirectory(), fileFilter));
            for (File file : files) {
                result.add(file);
            }

            File[] subDirectories = baseDirectory.listFiles(isDirectory());
            for (File subDirectory : subDirectories) {
                result.addAll(find(subDirectory, fileFilter));
            }
        }

        return result;
    }
}
