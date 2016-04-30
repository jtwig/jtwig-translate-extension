package org.jtwig.translate.message.source.localized.provider;

import org.jtwig.environment.Environment;
import org.jtwig.resource.reference.ResourceReference;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import static org.jtwig.translate.message.source.localized.provider.io.AndFileFilter.and;
import static org.jtwig.translate.message.source.localized.provider.io.NotDirectoryFileFilter.notDirectory;

public class FileLocalizedResourceProvider implements LocalizedResourceProvider {
    private final File baseDirectory;
    private final FileFilter fileFilter;

    public FileLocalizedResourceProvider(File baseDirectory, FileFilter fileFilter) {
        this.baseDirectory = baseDirectory;
        this.fileFilter = fileFilter;
    }

    @Override
    public Collection<ResourceReference> retrieve(Environment environment) {
        Collection<ResourceReference> result = new ArrayList<ResourceReference>();
        File[] files = baseDirectory.listFiles(and(notDirectory(), fileFilter));
        for (File file : files) {
            result.add(ResourceReference.file(file));
        }
        return result;
    }
}
