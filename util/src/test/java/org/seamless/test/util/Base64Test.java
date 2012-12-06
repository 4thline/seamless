package org.seamless.test.util;

import org.seamless.util.io.Base64Coder;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Christian Bauer
 */
public class Base64Test {

    @Test
    public void encodeDecode() {
        assertEquals(Base64Coder.encodeString("foo bar baz"), "Zm9vIGJhciBiYXo=");
        assertEquals(Base64Coder.decodeString("Zm9vIGJhciBiYXo="), "foo bar baz");
        assertEquals(Base64Coder.decodeString("Zm9v\nIGJh\rciBiYXo="), "foo bar baz");
    }

}
