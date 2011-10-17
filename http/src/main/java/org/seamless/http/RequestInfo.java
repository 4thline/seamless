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

package org.seamless.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Christian Bauer
 */
public class RequestInfo {

    public static void reportRequest(StringBuilder builder, HttpServletRequest req) {
        builder.append("Request: ");
        builder.append(req.getMethod());
        builder.append(' ');
        builder.append(req.getRequestURL());
        String queryString = req.getQueryString();
        if (queryString != null) {
            builder.append('?');
            builder.append(queryString);
        }

        builder.append(" - ");

        String sessionId = req.getRequestedSessionId();
        if (sessionId != null)
            builder.append("\nSession ID: ");
        if (sessionId == null) {
            builder.append("No Session");
        } else if (req.isRequestedSessionIdValid()) {
            builder.append(sessionId);
            builder.append(" (from ");
            if (req.isRequestedSessionIdFromCookie()) {
                builder.append("cookie)\n");
            } else if (req.isRequestedSessionIdFromURL()) {
                builder.append("url)\n");
            } else {
                builder.append("unknown)\n");
            }
        } else {
            builder.append("Invalid Session ID\n");
        }
    }

    public static void reportParameters(StringBuilder builder, HttpServletRequest req) {
        Enumeration names = req.getParameterNames();
        if (names == null) return;

        if (names.hasMoreElements()) {
            builder.append("Parameters:\n");
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String[] values = req.getParameterValues(name);
                if (values != null) {
                    for (String value : values) {
                        builder.append("    ").append(name).append(" = ").append(value).append('\n');
                    }
                }
            }
        }
    }

    public static void reportHeaders(StringBuilder builder, HttpServletRequest req) {
        Enumeration names = req.getHeaderNames();
        if (names == null) return;
        if (names.hasMoreElements()) {
            builder.append("Headers:\n");
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = req.getHeader(name);
                builder.append("    ").append(name).append(": ").append(value).append('\n');
            }
        }
    }

    public static void reportCookies(StringBuilder builder, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null)  return;
        int l = cookies.length;
        if (l > 0) {
            builder.append("Cookies:\n");
            for (int i = 0; i < l; ++i) {
                Cookie cookie = cookies[i];
                builder.append("    ").append(cookie.getName()).append(" = ").append(cookie.getValue()).append('\n');
            }
        }
    }

    public static void reportClient(StringBuilder builder, HttpServletRequest req) {
        builder.append("Remote Address: ").append(req.getRemoteAddr()).append("\n");
        if (!req.getRemoteAddr().equals(req.getRemoteHost()))
            builder.append("Remote Host: ").append(req.getRemoteHost()).append("\n");
        builder.append("Remote Port: ").append(req.getRemotePort()).append("\n");
        if (req.getRemoteUser() != null)
            builder.append("Remote User: ").append(req.getRemoteUser()).append("\n");
    }

}
