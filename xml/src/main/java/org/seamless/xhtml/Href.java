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

import java.net.URI;

/**
 * @author Christian Bauer
 */
public class Href {

    private URI uri;

    public Href(URI uri) {
        this.uri = uri;
    }

    public URI getURI() {
        return uri;
    }

    public static Href fromString(String string) {
        if (string == null) return null;
        string = string.replaceAll(" ", "%20");
        return new Href(URI.create(string));
    }

    @Override
    public String toString() {
        return getURI().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Href href = (Href) o;

        if (!uri.equals(href.uri)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
