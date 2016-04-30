package org.jtwig.translate.message.source.localized.provider.io;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

import static java.util.Arrays.asList;

public class AndFileFilter implements FileFilter {
    public static AndFileFilter and (FileFilter... filters) {
        return new AndFileFilter(asList(filters));
    }

    private final Collection<FileFilter> filters;

    public AndFileFilter(Collection<FileFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean accept(File pathname) {
        for (FileFilter filter : filters) {
            if (!filter.accept(pathname)) {
                return false;
            }
        }
        return true;
    }
}
