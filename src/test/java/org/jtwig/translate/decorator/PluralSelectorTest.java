package org.jtwig.translate.decorator;

import org.jtwig.model.position.Position;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;

public class PluralSelectorTest {
    private PluralSelector underTest = new PluralSelector(mock(Position.class), 10);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDecorate() throws Exception {
        expectedException.expectMessage(containsString("Unable to select option for 10 from '{1} ola'"));

        underTest.decorate("{1} ola");
    }
}