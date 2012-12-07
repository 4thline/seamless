 /*
 * Copyright (C) 2012 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
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
