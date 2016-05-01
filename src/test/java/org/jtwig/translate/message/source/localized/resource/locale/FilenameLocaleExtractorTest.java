package org.jtwig.translate.message.source.localized.resource.locale;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.resource.metadata.ResourceMetadata;
import org.junit.Test;

import java.net.URL;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilenameLocaleExtractorTest {
    private final RetrieveLocaleExpressionFromFilename expressionFromFilename = mock(RetrieveLocaleExpressionFromFilename.class);
    private FilenameLocaleExtractor underTest = new FilenameLocaleExtractor(expressionFromFilename);

    @Test
    public void extractLocaleNoUrl() throws Exception {
        Environment environment = mock(Environment.class);
        ResourceMetadata metadata = mock(ResourceMetadata.class);

        when(metadata.toUrl()).thenReturn(Optional.<URL>absent());

        Optional<Locale> result = underTest.extractLocale(environment, metadata);

        assertThat(result.isPresent(), is(false));
    }
    @Test
    public void extractLocaleNotFileProtocol() throws Exception {
        Environment environment = mock(Environment.class);
        ResourceMetadata metadata = mock(ResourceMetadata.class);

        when(metadata.toUrl()).thenReturn(Optional.of(new URL("http://localhost")));

        Optional<Locale> result = underTest.extractLocale(environment, metadata);

        assertThat(result.isPresent(), is(false));
    }
}