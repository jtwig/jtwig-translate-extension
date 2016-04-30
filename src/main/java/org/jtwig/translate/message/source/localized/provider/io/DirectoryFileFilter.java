package org.jtwig.translate.message.source.localized.provider.io;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFileFilter implements FileFilter {
    private static final DirectoryFileFilter INSTANCE = new DirectoryFileFilter();

    public static DirectoryFileFilter isDirectory () {
        return INSTANCE;
    }

    private DirectoryFileFilter () {}

    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }
}
