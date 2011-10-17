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

package org.seamless.test.http;

import org.seamless.http.Headers;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;

/**
 * @author Christian Bauer
 */
public class HeadersTest {

    @Test
    public void readHeaderLine() {
        String h = "HTTP/1.1 GET /foo/bar";
        ByteArrayInputStream is = new ByteArrayInputStream(h.getBytes());
        String  line = Headers.readLine(is);
        assertEquals(line, h);
    }

    @Test
    public void readHeaders() {
        String h = "HTTP/1.1 GET /foo/bar\r\nContent-Type: nothing";
        ByteArrayInputStream is = new ByteArrayInputStream(h.getBytes());
        String  line = Headers.readLine(is) + "\r\n" + Headers.readLine(is);
        assertEquals(line, h);
    }
}
