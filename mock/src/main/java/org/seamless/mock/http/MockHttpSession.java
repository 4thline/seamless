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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Bauer
 */
public class MockHttpSession implements HttpSession {

    protected Map<String, Object> attributes = new HashMap();
    protected boolean isInvalid;
    protected ServletContext servletContext;

    public MockHttpSession() {
    }

    public MockHttpSession(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public boolean isInvalid() {
        return isInvalid;
    }

    public long getCreationTime() {
        return 0;
    }

    public String getId() {
        return null;
    }

    public long getLastAccessedTime() {
        return 0;
    }

    private int maxInactiveInterval;

    public void setMaxInactiveInterval(int max) {
        maxInactiveInterval = max;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException();
    }

    public Object getAttribute(String attribute) {
        return attributes.get(attribute);
    }

    public Object getValue(String att) {
        return getAttribute(att);
    }

    public Enumeration getAttributeNames() {
        return new IteratorEnumeration(attributes.keySet().iterator());
    }

    public String[] getValueNames() {
        Set<String> strings = attributes.keySet();
        return strings.toArray(new String[strings.size()]);
    }

    public void setAttribute(String attribute, Object value) {
        if (value == null)
            attributes.remove(attribute);
        else
            attributes.put(attribute, value);
    }

    public void putValue(String attribute, Object value) {
        setAttribute(attribute, value);
    }

    public void removeAttribute(String attribute) {
        attributes.remove(attribute);
    }

    public void removeValue(String attribute) {
        removeAttribute(attribute);
    }

    public void invalidate() {
        attributes.clear();
        isInvalid = true;
    }

    public boolean isNew() {
        return false;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void clearAttributes() {
        attributes.clear();
    }
}
