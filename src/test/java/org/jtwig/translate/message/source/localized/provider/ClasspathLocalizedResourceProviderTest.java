package org.jtwig.translate.message.source.localized.provider;

import org.jtwig.environment.Environment;
import org.jtwig.resource.exceptions.ResourceException;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.translate.message.source.localized.provider.file.FileFinder;
import org.junit.Test;

import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClasspathLocalizedResourceProviderTest {
    private final ClassLoader classLoader = mock(ClassLoader.class);
    private ClasspathLocalizedResourceProvider underTest = new ClasspathLocalizedResourceProvider(classLoader, "", mock(FileFilter.class), mock(FileFinder.class));

    @Test
    public void retrieve() throws Exception {
        when(classLoader.getResources("")).thenReturn(new Vector<URL>(Collections.singletonList(new URL("http://localhost"))).elements());

        Collection<ResourceReference> result = underTest.retrieve(mock(Environment.class));

        assertThat(result.size(), is(0));
    }

    @Test(expected = ResourceException.class)
    public void retrieveException() throws Exception {
        when(classLoader.getResources("")).thenThrow(IOException.class);

        underTest.retrieve(mock(Environment.class));
    }
}