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
package org.seamless.mock.http;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Bauer
 */
public class MockServletContext implements ServletContext {

    protected File webappRoot;
    protected File webInfRoot;
    protected File webInfClassesRoot;

    private Map<String, String> initParameters = new HashMap();
    private Map<String, Object> attributes = new HashMap();

    public MockServletContext() {
    }

    public MockServletContext(File webappRoot, File webInfRoot, File webInfClassesRoot) {
        this.webappRoot = webappRoot;
        this.webInfRoot = webInfRoot;
        this.webInfClassesRoot = webInfClassesRoot;
    }

    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public ServletContext getContext(String name) {
        return this;
    }

    public int getMajorVersion() {
        return 2;
    }

    public int getMinorVersion() {
        return 4;
    }

    public String getMimeType(String arg) {
        return null;
    }

    public Set getResourcePaths(String name) {
        return null;
    }

    public URL getResource(String name) throws MalformedURLException {
        File file = getFile(name, webappRoot);

        if (file == null)
            file = getFile(name, webInfRoot);

        if (file == null)
            file = getFile(name, webInfClassesRoot);

        return file != null ? file.toURI().toURL() : null;
    }

    private static File getFile(String name, File root) {
        if (root == null)
            return null;

        if (name.startsWith("/"))
            name = name.substring(1);

        File f = new File(root, name);
        return !f.exists() ? null : f;
    }

    public InputStream getResourceAsStream(String name) {
        return getClass().getResourceAsStream(name);
    }

    public RequestDispatcher getRequestDispatcher(String url) {
        throw new UnsupportedOperationException();
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException();
    }

    public Enumeration getServlets() {
        return null;
    }

    public Enumeration getServletNames() {
        return null;
    }

    public void log(String msg) {

    }

    public void log(Exception ex, String msg) {

    }

    public void log(String msg, Throwable ex) {

    }

    public String getRealPath(String relativePath) {
        if (webappRoot != null) {
            return webappRoot.getAbsolutePath() + relativePath;
        } else {
            return relativePath;
        }
    }

    public String getServerInfo() {
        return null;
    }

    public String getInitParameter(String param) {
        return initParameters.get(param);
    }

    public Enumeration getInitParameterNames() {
        return new IteratorEnumeration(initParameters.keySet().iterator());
    }

    public Object getAttribute(String attribute) {
        return attributes.get(attribute);
    }

    public Enumeration getAttributeNames() {
        return new IteratorEnumeration(attributes.keySet().iterator());
    }

    public void setAttribute(String attribute, Object value) {
        if (value == null)
            attributes.remove(attribute);
        else
            attributes.put(attribute, value);
    }

    public void removeAttribute(String attribute) {
        attributes.remove(attribute);
    }

    public String getServletContextName() {
        return "Mock";
    }

    public String getContextPath() {
        return null;
    }
}
