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

import org.seamless.util.URIUtil;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URL;

import static org.testng.Assert.assertEquals;

/**
 * @author Christian Bauer
 */
public class URIUtilTest {

    @Test
    public void uriCreation() {

        URI base = URI.create("http://foo.bar/baz/");
        assertEquals(URIUtil.createAbsoluteURI(base, "/asdf.txt").toString(),
                     "http://foo.bar/asdf.txt"
        );

        base = URI.create("http://foo.bar/baz");
        assertEquals(URIUtil.createAbsoluteURI(base, "/asdf.txt").toString(),
                     "http://foo.bar/asdf.txt"
        );

        base = URI.create("http://foo.bar/baz/");
        assertEquals(URIUtil.createAbsoluteURI(base, "asdf.txt").toString(),
                     "http://foo.bar/baz/asdf.txt"
        );

        base = URI.create("http://foo.bar/baz");
        assertEquals(URIUtil.createAbsoluteURI(base, "asdf.txt").toString(),
                     "http://foo.bar/asdf.txt"
        );
    }

    @Test
    public void urlCreation() throws Exception {

        URL baseURL = new URL("http://foo.bar:0");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "asdf.txt").toString(),
                "http://foo.bar:0/asdf.txt"
        );

        baseURL = new URL("http://foo.bar:0/");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "asdf.txt").toString(),
                "http://foo.bar:0/asdf.txt"
        );

        baseURL = new URL("http://foo.bar");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "asdf.txt").toString(),
                "http://foo.bar/asdf.txt"
        );

        baseURL = new URL("http://foo.bar");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "/asdf.txt").toString(),
                "http://foo.bar/asdf.txt"
        );

        baseURL = new URL("http://foo.bar:0");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "asdf.txt").toString(),
                "http://foo.bar:0/asdf.txt"
        );

        baseURL = new URL("http://foo.bar:0/");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "asdf.txt").toString(),
                "http://foo.bar:0/asdf.txt"
        );

        baseURL = new URL("http://foo.bar");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "asdf.txt").toString(),
                "http://foo.bar/asdf.txt"
        );

        baseURL = new URL("http://foo.bar");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "/asdf.txt").toString(),
                "http://foo.bar/asdf.txt"
        );

        baseURL = new URL("http://foo.bar/baz");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "/asdf.txt").toString(),
                "http://foo.bar/asdf.txt"
        );

        baseURL = new URL("http://foo.bar/baz");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "asdf.txt").toString(),
                "http://foo.bar/asdf.txt"
        );

        baseURL = new URL("http://foo.bar/baz/");
        assertEquals(
                URIUtil.createAbsoluteURL(baseURL, "asdf.txt").toString(),
                "http://foo.bar/baz/asdf.txt"
        );
    }
}
