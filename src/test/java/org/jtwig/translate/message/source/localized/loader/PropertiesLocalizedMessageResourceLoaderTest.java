package org.jtwig.translate.message.source.localized.loader;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.resource.ResourceService;
import org.jtwig.resource.exceptions.ResourceException;
import org.jtwig.resource.exceptions.ResourceNotFoundException;
import org.jtwig.resource.metadata.ResourceMetadata;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.translate.message.source.localized.resource.locale.FilenameLocaleExtractor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class PropertiesLocalizedMessageResourceLoaderTest {
    private final FilenameLocaleExtractor filenameLocaleExtractor = mock(FilenameLocaleExtractor.class);
    private PropertiesLocalizedMessageResourceLoader underTest = new PropertiesLocalizedMessageResourceLoader(filenameLocaleExtractor);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void loadResourceNotFound() throws Exception {
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        ResourceReference resourceReference = mock(ResourceReference.class);
        ResourceService resourceService = mock(ResourceService.class);
        ResourceMetadata resourceMetadata = mock(ResourceMetadata.class);

        when(environment.getResourceEnvironment().getResourceService()).thenReturn(resourceService);
        when(resourceService.loadMetadata(resourceReference)).thenReturn(resourceMetadata);
        when(resourceMetadata.exists()).thenReturn(false);

        expectedException.expect(ResourceNotFoundException.class);
        expectedException.expectMessage(String.format("Resource '%s' not found", resourceReference));

        underTest.load(environment, resourceReference);
    }

    @Test
    public void loadNoLocaleFound() throws Exception {
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        ResourceReference resourceReference = mock(ResourceReference.class);
        ResourceService resourceService = mock(ResourceService.class);
        ResourceMetadata resourceMetadata = mock(ResourceMetadata.class);

        when(environment.getResourceEnvironment().getResourceService()).thenReturn(resourceService);
        when(resourceService.loadMetadata(resourceReference)).thenReturn(resourceMetadata);
        when(resourceMetadata.exists()).thenReturn(true);
        when(filenameLocaleExtractor.extractLocale(resourceMetadata)).thenReturn(Optional.<Locale>absent());
        when(resourceMetadata.load()).thenReturn(new ByteArrayInputStream("".getBytes()));

        expectedException.expect(ResourceException.class);
        expectedException.expectMessage(String.format("Could not extract locale from resource '%s'", resourceReference));

        underTest.load(environment, resourceReference);
    }

    @Test
    public void loadIOError() throws Exception {
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        ResourceReference resourceReference = mock(ResourceReference.class);
        ResourceService resourceService = mock(ResourceService.class);
        ResourceMetadata resourceMetadata = mock(ResourceMetadata.class);
        InputStream inputStream = mock(InputStream.class);

        when(environment.getResourceEnvironment().getResourceService()).thenReturn(resourceService);
        when(resourceService.loadMetadata(resourceReference)).thenReturn(resourceMetadata);
        when(resourceMetadata.exists()).thenReturn(true);
        when(filenameLocaleExtractor.extractLocale(resourceMetadata)).thenReturn(Optional.<Locale>absent());
        when(resourceMetadata.load()).thenReturn(inputStream);
        when(inputStream.read()).thenThrow(IOException.class);
        when(inputStream.read(Mockito.any(byte[].class))).thenThrow(IOException.class);
        when(inputStream.read(Mockito.any(byte[].class), anyInt(), anyInt())).thenThrow(IOException.class);

        expectedException.expect(ResourceException.class);
        expectedException.expectMessage(String.format("Cannot load properties file '%s'", resourceReference));

        underTest.load(environment, resourceReference);
    }
}