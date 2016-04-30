package org.jtwig.translate.locale;

import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JavaLocaleResolverTest {
    private JavaLocaleResolver underTest = new JavaLocaleResolver();

    @Test
    public void resolve() throws Exception {

        Locale result = underTest.resolve("pt-PT");

        assertThat(result.getCountry(), is("PT"));
    }
}