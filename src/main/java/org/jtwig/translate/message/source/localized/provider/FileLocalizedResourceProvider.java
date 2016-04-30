package org.jtwig.translate.message.source.localized.provider;

import org.jtwig.environment.Environment;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.translate.message.source.localized.provider.file.FileFinder;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

public class FileLocalizedResourceProvider implements LocalizedResourceProvider {
    private final File baseDirectory;
    private final FileFilter fileFilter;
    private final FileFinder fileFinder;

    public FileLocalizedResourceProvider(File baseDirectory, FileFilter fileFilter, FileFinder fileFinder) {
        this.baseDirectory = baseDirectory;
        this.fileFilter = fileFilter;
        this.fileFinder = fileFinder;
    }

    @Override
    public Collection<ResourceReference> retrieve(Environment environment) {
        Collection<ResourceReference> result = new ArrayList<ResourceReference>();
        Collection<File> files = fileFinder.find(baseDirectory, fileFilter);
        for (File file : files) {
            result.add(ResourceReference.file(file));
        }
        return result;
    }
}
