package com.blablabla.passwordswithcer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordsConverterTest {

    private static final String[] RUS = {"й", "ц", "у", "к", "е"};
    private static final String[] ENG = {"q", "w", "e", "r", "t"};

    private static final String[] SOURCES = {"", "некуцй", "йцукен", "зщшгне"};
    private static final String[] RESULT = {"", "ytrewq", "qwerty", "poiuyt"};

    private PasswordsConverter helper;

    @Before
    public void setUp() throws Exception {
        helper = new PasswordsConverter(RUS, ENG);
    }

    @Test
    public void convertRussianToLatin() {
        assertTrue("Error in test case", SOURCES.length == RESULT.length);
        assertEquals("", helper.convertRussianToLatin(""));
        for (int i = 0; i < SOURCES.length; i++) {
            assertEquals(RESULT[i], helper.convertRussianToLatin(SOURCES[i]));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertsIsThrows() {
        new PasswordsConverter(RUS, new String[]{});
    }

    @Test(expected = NullPointerException.class)
    public void testNotNull() {
        helper.convertRussianToLatin(null);
    }
}