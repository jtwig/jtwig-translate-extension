package org.jtwig.translate.function.extract;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.i18n.decorate.ReplacementMessageDecorator;
import org.jtwig.value.WrappedCollection;
import org.jtwig.value.convert.Converter;
import org.junit.Test;

import java.util.Collection;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReplacementsExtractorTest {
    private ReplacementsExtractor underTest = new ReplacementsExtractor();

    @Test
    public void testExtract() throws Exception {
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        Object input = new Object();

        when(environment.getValueEnvironment().getCollectionConverter().convert(input))
                .thenReturn(Converter.Result.defined(WrappedCollection.empty().add(null, null)));

        Optional<Collection<ReplacementMessageDecorator.Replacement>> result = underTest.extract(environment, input);

        assertEquals(emptyList(), result.get());
    }
}