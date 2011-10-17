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

import org.seamless.util.io.IO;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Christian Bauer
 */
public class HttpFetch {

    public static Representation<byte[]> fetchBinary(URL url) throws IOException {
        return fetchBinary(url, 500, 500);
    }

    public static Representation<byte[]> fetchBinary(URL url, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
        return fetch(url, connectTimeoutMillis, readTimeoutMillis, new RepresentationFactory<byte[]>() {
            public Representation<byte[]> createRepresentation(URLConnection urlConnection, InputStream is) throws IOException {
                return new Representation<byte[]>(urlConnection, IO.readBytes(is));
            }
        });
    }

    public static Representation<String> fetchString(URL url, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
        return fetch(url, connectTimeoutMillis, readTimeoutMillis, new RepresentationFactory<String>() {
            public Representation<String> createRepresentation(URLConnection urlConnection, InputStream is) throws IOException {
                return new Representation<String>(urlConnection, IO.readLines(is));
            }
        });
    }

    public static <E> Representation<E> fetch(URL url, int connectTimeoutMillis, int readTimeoutMillis, RepresentationFactory<E> factory)
            throws IOException {
        return fetch(url, "GET", connectTimeoutMillis, readTimeoutMillis, factory);
    }

    public static <E> Representation<E> fetch(URL url, String method, int connectTimeoutMillis, int readTimeoutMillis, RepresentationFactory<E> factory)
            throws IOException {

        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod(method);

            urlConnection.setConnectTimeout(connectTimeoutMillis);
            urlConnection.setReadTimeout(readTimeoutMillis);

            is = urlConnection.getInputStream();

            return factory.createRepresentation(urlConnection, is);

            // Any response code above 400 is going to throw IOException (or FileNotFoundException subclass)
        } catch (IOException ex) {
            if (urlConnection != null) {
                int responseCode = urlConnection.getResponseCode();
                throw new IOException("Fetching resource failed, returned status code: " + responseCode);
                //String responseBody = IO.readString(urlConnection.getErrorStream());
            }
            throw ex;
        } finally {
            if (is != null) is.close();
        }
    }

    public static interface RepresentationFactory<E> {
        public Representation<E> createRepresentation(URLConnection urlConnection, InputStream is) throws IOException;
    }

    public static void validate(URL url) throws IOException {
        fetch(url, "HEAD", 500, 500, new RepresentationFactory() {
            public Representation createRepresentation(URLConnection urlConnection, InputStream is) throws IOException {
                return new Representation(urlConnection, null);
            }
        });
    }
}
