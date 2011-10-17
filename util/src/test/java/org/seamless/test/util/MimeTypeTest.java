/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.seamless.test.util;

import org.seamless.util.MimeType;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
/**
 * @author Christian Bauer
 */
public class MimeTypeTest {

    @Test
    public void parseMimeTypeWithParameters() {
        String s = "audio/L16;rate=44100;id=\"ABC@host.com\";channels=1";
        MimeType mt = MimeType.valueOf(s);
        assertEquals(mt.getType(), "audio");
        assertEquals(mt.getSubtype(), "L16");
        assertEquals(mt.getParameters().size(), 3);

        assertEquals(mt.getParameters().get("rate"), "44100");
        assertEquals(mt.getParameters().get("id"), "ABC@host.com");
        assertEquals(mt.getParameters().get("channels"), "1");
    }

    @Test
    public void parseMimeTypeIllegalWhitespace() {
        MimeType mt = MimeType.valueOf("foo/bar ;charset=\"utf-8\"");
        assertEquals(mt.getType(), "foo");
        assertEquals(mt.getSubtype(), "bar");
        assertEquals(mt.getParameters().size(), 1);

        mt = MimeType.valueOf("foo/bar; charset=\"utf-8\"");
        assertEquals(mt.getType(), "foo");
        assertEquals(mt.getSubtype(), "bar");
        assertEquals(mt.getParameters().size(), 1);
    }
}
