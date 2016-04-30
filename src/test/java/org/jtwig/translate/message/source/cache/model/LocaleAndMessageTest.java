package org.jtwig.translate.message.source.cache.model;

import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LocaleAndMessageTest {

    @Test
    public void equalsSameInstance() throws Exception {
        LocaleAndMessage instance = new LocaleAndMessage(Locale.CHINA, "message");

        assertThat(instance.equals(instance), is(true));
    }

    @Test
    public void equalsNull() throws Exception {
        LocaleAndMessage instance = new LocaleAndMessage(Locale.CHINA, "message");

        assertThat(instance.equals(null), is(false));
    }

    @Test
    public void equalsDistinctType() throws Exception {
        LocaleAndMessage instance = new LocaleAndMessage(Locale.CHINA, "message");

        assertThat(instance.equals("asd"), is(false));
    }

    @Test
    public void equalsDistinctLocale() throws Exception {
        Locale locale = Locale.CHINA;
        String message = "message";
        LocaleAndMessage instance = new LocaleAndMessage(locale, message);

        assertThat(instance.equals(new LocaleAndMessage(Locale.CANADA, message)), is(false));
    }

    @Test
    public void equalsDistinctMessage() throws Exception {
        Locale locale = Locale.CHINA;
        String message = "message";
        LocaleAndMessage instance = new LocaleAndMessage(locale, message);

        assertThat(instance.equals(new LocaleAndMessage(locale, "one")), is(false));
    }

    @Test
    public void equalsHappy() throws Exception {
        Locale locale = Locale.CHINA;
        String message = "message";
        LocaleAndMessage instance = new LocaleAndMessage(locale, message);

        assertThat(instance.equals(new LocaleAndMessage(Locale.CHINA, "message")), is(true));
    }
}