package org.jtwig.translate.message.source.localized.provider;

import org.jtwig.environment.Environment;
import org.jtwig.resource.reference.ResourceReference;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

import static org.jtwig.translate.message.source.localized.provider.io.AndFileFilter.and;
import static org.jtwig.translate.message.source.localized.provider.io.DirectoryFileFilter.isDirectory;
import static org.jtwig.translate.message.source.localized.provider.io.NotDirectoryFileFilter.notDirectory;

public class RecursiveFileLocalizedResourceProvider implements LocalizedResourceProvider {
    private final File baseDirectory;
    private final FileFilter fileFilter;

    public RecursiveFileLocalizedResourceProvider(File baseDirectory, FileFilter fileFilter) {
        this.baseDirectory = baseDirectory;
        this.fileFilter = fileFilter;
    }

    @Override
    public Collection<ResourceReference> retrieve(Environment environment) {
        return retrieveRecursively(baseDirectory);
    }

    private Collection<ResourceReference> retrieveRecursively (File directory) {
        Collection<ResourceReference> result = new ArrayList<ResourceReference>();

        File[] files = directory.listFiles(and(notDirectory(), fileFilter));
        for (File file : files) {
            result.add(ResourceReference.file(file));
        }

        File[] subDirectories = directory.listFiles(isDirectory());
        for (File subDirectory : subDirectories) {
            result.addAll(retrieveRecursively(subDirectory));
        }

        return result;
    }
}
