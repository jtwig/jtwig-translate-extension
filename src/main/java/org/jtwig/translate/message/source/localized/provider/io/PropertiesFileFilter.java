package org.jtwig.translate.message.source.localized.provider.io;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;

public class PropertiesFileFilter implements FileFilter {
    private static final PropertiesFileFilter INSTANCE = new PropertiesFileFilter();
    public static PropertiesFileFilter properties () {
        return INSTANCE;
    }

    private PropertiesFileFilter() {}

    @Override
    public boolean accept(File pathname) {
        String name = pathname.getName();
        return StringUtils.isNotBlank(name) && name.matches(".*\\.\\w\\w+\\.properties$");
    }
}
