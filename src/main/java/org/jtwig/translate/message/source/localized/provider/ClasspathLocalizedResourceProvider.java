package org.jtwig.translate.message.source.localized.provider;

import org.jtwig.environment.Environment;
import org.jtwig.resource.exceptions.ResourceException;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.translate.message.source.localized.provider.file.FileFinder;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public class ClasspathLocalizedResourceProvider implements LocalizedResourceProvider {
    public static final String FILE_PROTOCOL = "file";
    private final ClassLoader classLoader;
    private final String basePackage;
    private final FileFilter fileFilter;
    private final FileFinder fileFinder;

    public ClasspathLocalizedResourceProvider(ClassLoader classLoader, String basePackage, FileFilter fileFilter, FileFinder fileFinder) {
        this.classLoader = classLoader;
        this.basePackage = basePackage;
        this.fileFilter = fileFilter;
        this.fileFinder = fileFinder;
    }

    @Override
    public Collection<ResourceReference> retrieve(Environment environment) {
        Collection<ResourceReference> result = new ArrayList<>();
        try {
            Enumeration<URL> resources = classLoader.getResources("");
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (FILE_PROTOCOL.contains(resource.getProtocol())) {
                    File baseDirectory = new File(resource.getFile(), basePackage.replace(".", File.separator));
                    Collection<File> files = fileFinder.find(baseDirectory, fileFilter);
                    for (File file : files) {
                        result.add(ResourceReference.file(file));
                    }
                }
            }
        } catch (IOException e) {
            throw new ResourceException("Cannot load classpath root directories", e);
        }
        return result;
    }
}
