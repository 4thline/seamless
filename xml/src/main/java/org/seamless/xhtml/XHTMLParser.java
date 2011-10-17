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

import org.seamless.xml.DOMParser;
import org.seamless.xml.NamespaceContextMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Christian Bauer
 */
public class XHTMLParser extends DOMParser<XHTML> {

    public XHTMLParser() {
        super(XHTML.createSchemaSources());
    }

    @Override
    protected XHTML createDOM(Document document) {
        return document != null ? new XHTML(document) : null;
    }

    public void checkDuplicateIdentifiers(XHTML document) throws IllegalStateException {
        final Set<String> identifiers = new HashSet();
        accept(document.getW3CDocument().getDocumentElement(), new XHTMLParser.NodeVisitor(Node.ELEMENT_NODE) {
            @Override
            public void visit(Node node) {
                Element element = (Element) node;

                String id = element.getAttribute(XHTML.ATTR.id.name());
                if (!"".equals(id)) {
                    if (identifiers.contains(id)) {
                        throw new IllegalStateException("Duplicate identifier, override/change value: " + id);
                    }
                    identifiers.add(id);
                }
            }
        });

    }

    public NamespaceContextMap createDefaultNamespaceContext(String... optionalPrefixes) {
        NamespaceContextMap ctx = new NamespaceContextMap() {
            @Override
            protected String getDefaultNamespaceURI() {
                return XHTML.NAMESPACE_URI;
            }
        };
        for (String optionalPrefix : optionalPrefixes) {
            ctx.put(optionalPrefix, XHTML.NAMESPACE_URI);
        }
        return ctx;
    }

    public XPath createXPath() {
        return super.createXPath(createDefaultNamespaceContext(XHTMLElement.XPATH_PREFIX));
    }
    

}
