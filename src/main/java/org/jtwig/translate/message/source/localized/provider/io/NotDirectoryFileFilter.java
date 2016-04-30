package org.jtwig.translate.message.source.localized.provider.io;

import java.io.File;
import java.io.FileFilter;

public class NotDirectoryFileFilter implements FileFilter {
    private static final NotDirectoryFileFilter INSTANCE = new NotDirectoryFileFilter();

    public static NotDirectoryFileFilter notDirectory () {
        return INSTANCE;
    }

    private NotDirectoryFileFilter() {
    }

    @Override
    public boolean accept(File pathname) {
        return !pathname.isDirectory();
    }
}
