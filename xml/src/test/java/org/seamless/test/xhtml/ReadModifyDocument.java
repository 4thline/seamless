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

import org.seamless.xhtml.Body;
import org.seamless.xhtml.Head;
import org.seamless.xhtml.XHTML;
import org.seamless.xhtml.XHTMLElement;
import org.seamless.xhtml.XHTMLParser;
import org.testng.annotations.Test;

import javax.xml.xpath.XPath;
import java.net.URI;

import static org.testng.Assert.assertEquals;

/**
 * @author Christian Bauer
 */
public class ReadModifyDocument {

    protected XHTMLParser createParser() {
        return new XHTMLParser();
    }

    @Test
    public void readDocument() throws Exception {
        XHTMLParser parser = createParser();
        XPath xpath = parser.createXPath();

        XHTML doc = parser.parse(getClass().getResource("/org/seamless/test/xhtml/simple.xhtml"), true);

        assertEquals(doc.getRoot(xpath).getW3CElement().getNodeName(), XHTML.ELEMENT.html.name());

        Head head = doc.getRoot(xpath).getHead();

        assertEquals(head.getHeadTitle().getContent(), "This is a test");

        assertEquals(head.getLinks().length, 1);
        assertEquals(head.getLinks()[0].getAttribute(XHTML.ATTR.id), "foo");
        assertEquals(head.getLinks()[0].getAttribute(XHTML.ATTR.type), "some/type");
        assertEquals(head.getLinks()[0].getHref().getURI(), URI.create("http://some/uri/path"));

        assertEquals(head.getDocumentStyles().length, 1);
        assertEquals(head.getDocumentStyles()[0].getAttribute(XHTML.ATTR.type), "text/css");
        assertEquals(head.getDocumentStyles()[0].getAttribute(XHTML.ATTR.title), "foobar");

        assertEquals(head.getMetas().length, 1);
        assertEquals(head.getMetas()[0].getAttribute(XHTML.ATTR.name), "foo");
        assertEquals(head.getMetas()[0].getAttribute(XHTML.ATTR.content), "bar");
        assertEquals(head.getMetas()[0].getAttribute(XHTML.ATTR.scheme), "foobarbaz");

        Body body = doc.getRoot(xpath).getBody();

        assertEquals(body.findChildren(XHTML.ELEMENT.div).length, 2);
        assertEquals(body.findChildrenWithClass(XHTML.ELEMENT.div, "abc").length, 2);
        assertEquals(body.findChildrenWithClass(XHTML.ELEMENT.div, "aaa").length, 1);
        assertEquals(body.findChildrenWithClass(XHTML.ELEMENT.div, "abcd").length, 0);
        assertEquals(body.findChildrenWithClass(XHTML.ELEMENT.div, null).length, 2);

        assertEquals(body.getChildren()[0].findChildrenWithClass(XHTML.ELEMENT.div, "abc").length, 1);

        assertEquals(body.findAllAnchors(null).length, 2);
        assertEquals(body.findAllAnchors(null, "foobar").length, 1);

        assertEquals(body.getChildren().length, 1);

        assertEquals(body.getChildren()[0].getConstant(), XHTML.ELEMENT.div);
        assertEquals(body.getChildren()[0].getElementName(), XHTML.ELEMENT.div.name());
        assertEquals(body.getChildren()[0].getAttribute(XHTML.ATTR.id), "one");
        assertEquals(body.getChildren()[0].getClasses()[0], "aaa");

        assertEquals(body.getChildren()[0].getChildren()[0].getElementName(), "h1");

        assertEquals(body.getChildren()[0].getChildren()[1].getConstant(), XHTML.ELEMENT.div);
        assertEquals(body.getChildren()[0].getChildren()[1].getAttribute(XHTML.ATTR.id), "two");
        assertEquals(body.getChildren()[0].getChildren()[1].getClasses()[0], "abc");
        assertEquals(body.getChildren()[0].getChildren()[1].getClasses()[1], "def");

        XHTMLElement anchor = body.getChildren()[0].getChildren()[1].getFirstChild(XHTML.ELEMENT.a);
        assertEquals(anchor.getElementName(), "a");
        assertEquals(anchor.getOptions().length, 3);
        assertEquals(anchor.getOptions()[0].getKey(), "one");
        assertEquals(anchor.getOptions()[0].getFirstValue(), "1");
        assertEquals(anchor.getOptions()[1].getKey(), "two");
        assertEquals(anchor.getOptions()[1].getFirstValue(), "2");
        assertEquals(anchor.getOptions()[2].getKey(), "three");
        assertEquals(anchor.getOptions()[2].getFirstValue(), "3");
        assertEquals(anchor.getAttribute(XHTML.ATTR.href), "/foo/bar.html");
    }

    @Test
    public void modifyDocument() throws Exception {
        XHTMLParser parser = createParser();
        XPath xpath = parser.createXPath();

        XHTML doc = parser.parse(getClass().getResource("/org/seamless/test/xhtml/simple.xhtml"), true);

        // Change the title
        Head head = doc.getRoot(xpath).getHead();
        head.getHeadTitle().setContent("My Title");
        assertEquals(head.getHeadTitle().getContent(), "My Title");

        // Copy elements from another document
        XHTML second = parser.parse(getClass().getResource("/org/seamless/test/xhtml/second.xhtml"), true);
        Head secondHead = second.getRoot(xpath).getHead();
        for (XHTMLElement linkElement : secondHead.getChildren(XHTML.ELEMENT.link)) {
            head.appendChild(linkElement, true);
        }
        assertEquals(head.getChildren(XHTML.ELEMENT.link).length, 2);

        // Replace an element with one from another document
        Body body = doc.getRoot(xpath).getBody();
        body.replaceEqualChild(second.getRoot(xpath).getBody(), "two");
        XHTMLElement two = body.findChildWithIdentifier("two");
        assertEquals(two.getChildren("h2").length, 1);
        assertEquals(two.getChildren("h2")[0].getContent(), "Another Test");

        // Remove child
        XHTMLElement one = body.findChildWithIdentifier("one");
        one.removeChild(one.findChildWithIdentifier("two"));
        assertEquals(one.getChildren().length, 1);

    }
}
