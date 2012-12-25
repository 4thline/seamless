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
package org.seamless.cdi.weld;

import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.exceptions.IllegalStateException;
import org.jboss.weld.xml.BeansXmlHandler;
import org.jboss.weld.xml.BeansXmlParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.enterprise.inject.spi.BeanManager;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.jboss.weld.bootstrap.spi.BeansXml.EMPTY_BEANS_XML;
import static org.jboss.weld.logging.messages.XmlMessage.*;

/**
 * Override how the <code>InputStream</code> of the XML file is opened.
 *
 * <p>
 *     If a <code>beans.xml</code> URL has several bangs ("!") in its path, we
 *     assume it's in a nested JAR file. The <code>beans.xml</code> is therefore
 *     extracted before its <code>InputStream</code> is passed to the superclass.
 * </p>
 * <p>
 *     An alternative would be a new URL protocol handler, but this is even more
 *     painful than this hack.
 * </p>
 * <p>
 *     Unfortunately, someone thought <code>private</code> visibility is a great
 *     idea, so much code of the superclass had to be copied. The license of this
 *     copied code is unaffected by this file.
 * </p>
 *
 * @author Christian Bauer
 */
public class SeamlessBeansXmlParser extends BeansXmlParser {

    protected InputStream getInputStream(URL url) throws IOException {
        if ("jar".equals(url.getProtocol())
            && url.getPath().indexOf("!") != url.getPath().lastIndexOf("!")) {

            String[] paths = url.getPath().split("!");
            File file = new File(URI.create(paths[0]));
            InputStream inputStream = new FileInputStream(file);
            try {
                byte[] extracted = extractNestedFile(paths, 1, inputStream);
                if (extracted != null) {
                    return new ByteArrayInputStream(extracted);
                } else {
                    throw new IOException("Unable to extract: " + url);
                }
            } finally {
                inputStream.close();
            }
        } else {
            return url.openStream();
        }
    }

    protected byte[] extractNestedFile(String[] paths,
                                       int current,
                                       InputStream inputStream) throws IOException {
        byte[] result = null;
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (entry.getName().equals(paths[current].substring(1))) {
                if (current < paths.length - 1) {
                    result = extractNestedFile(paths, current + 1, zipInputStream);
                } else {
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    int bytesRead;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                    result = outStream.toByteArray();
                }
            }
            zipInputStream.closeEntry();
        }
        return result;
    }

    /* ########################################### */
    /*  FOLLOWING CODE COPIED FROM BeansXmlParser  */
    /* ########################################### */

    private static final InputSource[] EMPTY_INPUT_SOURCE_ARRAY = new InputSource[0];

    private static boolean disableValidating;

    static {
        try {
            disableValidating = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    return Boolean.getBoolean("org.jboss.weld.xml.disableValidating");
                }
            });
        } catch (Throwable ignored) {
        }
    }

    public BeansXml parse(final URL beansXml) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(!disableValidating);
        factory.setNamespaceAware(true);
        if (beansXml == null) {
            throw new IllegalStateException(LOAD_ERROR, "unknown");
        }
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
        } catch (SAXException e) {
            throw new IllegalStateException(CONFIGURATION_ERROR, e);
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(CONFIGURATION_ERROR, e);
        }
        InputStream beansXmlInputStream = null;
        try {

            // ### DON'T USE URL.openStream() IF YOU WANT YOUR CODE TO BE EXTENSIBLE!
            beansXmlInputStream = getInputStream(beansXml);

            InputSource source = new InputSource(beansXmlInputStream);
            if (source.getByteStream().available() == 0) {
                // The file is just acting as a marker file
                return EMPTY_BEANS_XML;
            }
            BeansXmlHandler handler = new BeansXmlHandler(beansXml);

            try {
                parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
                parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", loadXsds());
            } catch (IllegalArgumentException e) {
                // No op, we just don't validate the XML
            } catch (SAXNotRecognizedException e) {
                // No op, we just don't validate the XML
            } catch (SAXNotSupportedException e) {
                // No op, we just don't validate the XML
            }

            parser.parse(source, handler);

            return handler.createBeansXml();
        } catch (IOException e) {
            throw new IllegalStateException(LOAD_ERROR, e, beansXml);
        } catch (SAXException e) {
            throw new IllegalStateException(PARSING_ERROR, beansXml, e);
        } finally {
            if (beansXmlInputStream != null) {
                try {
                    beansXmlInputStream.close();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    private static InputSource[] loadXsds() {
        List<InputSource> xsds = new ArrayList<InputSource>();
        // The Weld xsd
        InputSource weldXsd = loadXsd("beans_1_1.xsd", BeansXmlParser.class.getClassLoader());
        // The CDI Xsd
        InputSource cdiXsd = loadXsd("beans_1_0.xsd", BeanManager.class.getClassLoader());
        if (weldXsd != null) {
            xsds.add(weldXsd);
        }
        if (cdiXsd != null) {
            xsds.add(cdiXsd);
        }
        return xsds.toArray(EMPTY_INPUT_SOURCE_ARRAY);
    }


    private static InputSource loadXsd(String name, ClassLoader classLoader) {
        InputStream in = classLoader.getResourceAsStream(name);
        if (in == null) {
            return null;
        } else {
            return new InputSource(in);
        }
    }
}
