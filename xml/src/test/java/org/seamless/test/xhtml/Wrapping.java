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

package org.seamless.test.xhtml;

import org.seamless.xhtml.XHTML;
import org.seamless.xhtml.XHTMLElement;
import org.seamless.xhtml.XHTMLParser;
import org.testng.annotations.Test;

import javax.xml.xpath.XPath;

import static org.testng.Assert.assertEquals;

/**
 * @author Christian Bauer
 */
public class Wrapping {

    protected XHTMLParser createParser() {
        return new XHTMLParser();
    }

    @Test
    public void workFragment() throws Exception {
        XHTMLParser parser = createParser();
        XPath xpath = parser.createXPath();

        XHTML xhtml = parser.createDocument();

        XHTMLElement root =
                xhtml.createRoot(xpath, XHTML.ELEMENT.div)
                        .setAttribute(XHTML.ATTR.CLASS, "foo")
                        .setAttribute(XHTML.ATTR.id, "div1");

        root.createChild(XHTML.ELEMENT.a)
                .setAttribute(XHTML.ATTR.CLASS, "test1 bar")
                .setAttribute(XHTML.ATTR.href, "http://test1.foo.bar");

        root.createChild(XHTML.ELEMENT.a)
                .setAttribute(XHTML.ATTR.CLASS, "test2 bar")
                .setAttribute(XHTML.ATTR.href, "http://test2.foo.bar");

        assertEquals(xhtml.getRoot(xpath).findAllAnchors(null, "bar").length, 2);
    }

    @Test
    public void wrapParse() throws Exception {
        String frag =
                "<a href=\"http://test1.foo.bar\">test1</a>" +
                "<a href=\"http://test2.foo.bar\">test2</a>";

        XHTMLParser parser = createParser();
        XPath xpath = parser.createXPath();

        // Create wrapper document with XHTML default namespace, XPath query works on XHTML namespace context
        XHTML xhtml = parser.parse(XHTMLParser.wrap("div", XHTML.NAMESPACE_URI, frag), false);
        assertEquals(xhtml.getRoot(xpath).findAllAnchors(null, null).length, 2);

        // No namespace, no query hits
        xhtml = parser.parse(XHTMLParser.wrap("div", frag), false);
        assertEquals(xhtml.getRoot(xpath).findAllAnchors(null, null).length, 0);
    }

}
