package org.jtwig.translate.message.source.localized.provider.io;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PropertiesFileFilterTest {
    private PropertiesFileFilter underTest = PropertiesFileFilter.properties();

    @Test
    public void accept() throws Exception {
        assertFalse(underTest.accept(new File("start.properties")));
        assertFalse(underTest.accept(new File("file.pt.txt")));
        assertFalse(underTest.accept(new File("file.t.properties")));
        assertFalse(underTest.accept(new File("")));
        assertTrue(underTest.accept(new File("file.it.properties")));
    }
}