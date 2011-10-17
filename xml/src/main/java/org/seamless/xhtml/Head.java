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
package org.seamless.xhtml;

import org.w3c.dom.Element;

import javax.xml.xpath.XPath;

/**
 * @author Christian Bauer
 */
public class Head extends XHTMLElement {

    public Head(XPath xpath, Element element) {
        super(xpath, element);
    }

    public XHTMLElement getHeadTitle() {
        return CHILD_BUILDER.firstChildOrNull(XHTML.ELEMENT.title.name());
    }

    public Link[] getLinks() {
        return new ArrayBuilder<Link>(this) {
            @Override
            public Link build(Element element) {
                return new Link(getXpath(), element);
            }

            @Override
            public Link[] newChildrenArray(int length) {
                return new Link[length];
            }
        }.getChildElements(XHTML.ELEMENT.link.name());
    }

    public Meta[] getMetas() {
        return new ArrayBuilder<Meta>(this) {
            @Override
            public Meta build(Element element) {
                return new Meta(getXpath(), element);
            }

            @Override
            public Meta[] newChildrenArray(int length) {
                return new Meta[length];
            }
        }.getChildElements(XHTML.ELEMENT.meta.name());
    }

    public XHTMLElement[] getDocumentStyles() {
        return CHILD_BUILDER.getChildElements(XHTML.ELEMENT.style.name());
    }

    public XHTMLElement[] getScripts() {
        return CHILD_BUILDER.getChildElements(XHTML.ELEMENT.script.name());
    }

}