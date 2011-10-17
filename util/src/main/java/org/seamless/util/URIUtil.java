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
package org.seamless.util;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.BitSet;

/**
 * @author Christian Bauer
 */
public class URIUtil {

    /**
     * Guarantees that the returned URI is absolute, no matter what the argument is.
     *
     * @param base An absolute base URI, can be null!
     * @param uri  A string that either represents a relative or an already absolute URI
     * @return An absolute URI
     * @throws IllegalArgumentException If the base URI is null and the given URI string is not absolute
     */
    public static URI createAbsoluteURI(URI base, String uri) throws IllegalArgumentException {
        return createAbsoluteURI(base, URI.create(uri));
    }

    public static URI createAbsoluteURI(URI base, URI relativeOrNot) throws IllegalArgumentException {
        if (base == null && !relativeOrNot.isAbsolute()) {
            throw new IllegalArgumentException("Base URI is null and given URI is not absolute");
        } else if (base == null && relativeOrNot.isAbsolute()) {
            return relativeOrNot;
        } else {
            assert base != null;
            // If the given base URI has no path we give it a root path
            if (base.getPath().length() == 0) {
                try {
                    base = new URI(base.getScheme(), base.getAuthority(), "/", base.getQuery(), base.getFragment());
                } catch (Exception ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
            return base.resolve(relativeOrNot);
        }
    }

    public static URL createAbsoluteURL(URL base, String uri) throws IllegalArgumentException {
        return createAbsoluteURL(base, URI.create(uri));
    }

    public static URL createAbsoluteURL(URL base, URI relativeOrNot) throws IllegalArgumentException {

        if (base == null && !relativeOrNot.isAbsolute()) {
            throw new IllegalArgumentException("Base URL is null and given URI is not absolute");
        } else if (base == null && relativeOrNot.isAbsolute()) {
            try {
                return relativeOrNot.toURL();
            } catch (Exception ex) {
                throw new IllegalArgumentException("Base URL was null and given URI can't be converted to URL");
            }
        } else {
            try {
                assert base != null;
                URI baseURI = base.toURI();
                URI absoluteURI = createAbsoluteURI(baseURI, relativeOrNot);
                return absoluteURI.toURL();
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Base URL is not an URI, or can't create absolute URI (null?), " +
                                "or absolute URI can not be converted to URL", ex);
            }
        }
    }

    public static URL createAbsoluteURL(URI base, URI relativeOrNot) throws IllegalArgumentException {
        try {
            return createAbsoluteURI(base, relativeOrNot).toURL();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Absolute URI can not be converted to URL", ex);
        }
    }

    public static URL createAbsoluteURL(InetAddress address, int localStreamPort, URI relativeOrNot) throws IllegalArgumentException {
        try {
            if (address instanceof Inet6Address) {
                return createAbsoluteURL(new URL("http://[" + address.getHostAddress() + "]:" + localStreamPort), relativeOrNot);
            } else if (address instanceof Inet4Address) {
                return createAbsoluteURL(new URL("http://" + address.getHostAddress() + ":" + localStreamPort), relativeOrNot);
            } else {
                throw new IllegalArgumentException("InetAddress is neither IPv4 nor IPv6: " + address);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Address, port, and URI can not be converted to URL", ex);
        }
    }

    public static URI createRelativePathURI(URI uri) {
        assertRelativeURI("Given", uri);

        // Remove all "./" segments
        URI normalizedURI = uri.normalize();

        // Remove all "../" segments
        String uriString = normalizedURI.toString();
        int idx;
        while ((idx = uriString.indexOf("../")) != -1)
            uriString = uriString.substring(0, idx) + uriString.substring(idx + 3);

        // Make relative path
        while (uriString.startsWith("/"))
            uriString = uriString.substring(1);

        return URI.create(uriString);
    }

    public static URI createRelativeURI(URI base, URI full) {
        return base.relativize(full);
    }

    public static URI createRelativeURI(URL base, URL full) throws IllegalArgumentException {
        try {
            return createRelativeURI(base.toURI(), full.toURI());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Can't convert base or full URL to URI", ex);
        }
    }

    public static URI createRelativeURI(URI base, URL full) throws IllegalArgumentException {
        try {
            return createRelativeURI(base, full.toURI());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Can't convert full URL to URI", ex);
        }
    }

    public static URI createRelativeURI(URL base, URI full) throws IllegalArgumentException {
        try {
            return createRelativeURI(base.toURI(), full);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Can't convert base URL to URI", ex);
        }
    }

    public static boolean isAbsoluteURI(String s) {
        URI uri = URI.create(s);
        return uri.isAbsolute();
    }

    public static void assertRelativeURI(String what, URI uri) {
        if (uri.isAbsolute()) {
            throw new IllegalArgumentException(what + " URI must be relative, without scheme and authority");
        }
    }

    public static URL toURL(URI uri) {
        if (uri == null) return null;
        try {
            return uri.toURL();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static URI toURI(URL url) {
        if (url == null) return null;
        try {
            return url.toURI();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String percentEncode(String s) {
        if (s == null) return "";
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String percentDecode(String s) {
        if (s == null) return "";
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * The following code is taken from Resteasy, probably LGPL. It is also mentioned in this blog entry by
     * the author: http://www.lunatech-research.com/archives/2009/02/03/what-every-web-developer-must-know-about-url-encoding
     */


    /**
     * gen-delims = ":" / "/" / "?" / "#" / "[" / "]" / "@"
     */
    public final static BitSet GEN_DELIMS = new BitSet();

    static {
        GEN_DELIMS.set(':');
        GEN_DELIMS.set('/');
        GEN_DELIMS.set('?');
        GEN_DELIMS.set('#');
        GEN_DELIMS.set('[');
        GEN_DELIMS.set(']');
        GEN_DELIMS.set('@');
    }

    /**
     * sub-delims = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
     */
    public final static BitSet SUB_DELIMS = new BitSet();

    static {
        SUB_DELIMS.set('!');
        SUB_DELIMS.set('$');
        SUB_DELIMS.set('&');
        SUB_DELIMS.set('\'');
        SUB_DELIMS.set('(');
        SUB_DELIMS.set(')');
        SUB_DELIMS.set('*');
        SUB_DELIMS.set('+');
        SUB_DELIMS.set(',');
        SUB_DELIMS.set(';');
        SUB_DELIMS.set('=');
    }

    /**
     * reserved = gen-delims | sub-delims
     */
    public final static BitSet RESERVED = new BitSet();

    static {
        RESERVED.or(GEN_DELIMS);
        RESERVED.or(SUB_DELIMS);
    }

    /**
     * lowalpha = "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" |
     * "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
     */
    public final static BitSet LOW_ALPHA = new BitSet();

    static {
        LOW_ALPHA.set('a');
        LOW_ALPHA.set('b');
        LOW_ALPHA.set('c');
        LOW_ALPHA.set('d');
        LOW_ALPHA.set('e');
        LOW_ALPHA.set('f');
        LOW_ALPHA.set('g');
        LOW_ALPHA.set('h');
        LOW_ALPHA.set('i');
        LOW_ALPHA.set('j');
        LOW_ALPHA.set('k');
        LOW_ALPHA.set('l');
        LOW_ALPHA.set('m');
        LOW_ALPHA.set('n');
        LOW_ALPHA.set('o');
        LOW_ALPHA.set('p');
        LOW_ALPHA.set('q');
        LOW_ALPHA.set('r');
        LOW_ALPHA.set('s');
        LOW_ALPHA.set('t');
        LOW_ALPHA.set('u');
        LOW_ALPHA.set('v');
        LOW_ALPHA.set('w');
        LOW_ALPHA.set('x');
        LOW_ALPHA.set('y');
        LOW_ALPHA.set('z');
    }

    /**
     * upalpha = "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" |
     * "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z"
     */
    public final static BitSet UP_ALPHA = new BitSet();

    static {
        UP_ALPHA.set('A');
        UP_ALPHA.set('B');
        UP_ALPHA.set('C');
        UP_ALPHA.set('D');
        UP_ALPHA.set('E');
        UP_ALPHA.set('F');
        UP_ALPHA.set('G');
        UP_ALPHA.set('H');
        UP_ALPHA.set('I');
        UP_ALPHA.set('J');
        UP_ALPHA.set('K');
        UP_ALPHA.set('L');
        UP_ALPHA.set('M');
        UP_ALPHA.set('N');
        UP_ALPHA.set('O');
        UP_ALPHA.set('P');
        UP_ALPHA.set('Q');
        UP_ALPHA.set('R');
        UP_ALPHA.set('S');
        UP_ALPHA.set('T');
        UP_ALPHA.set('U');
        UP_ALPHA.set('V');
        UP_ALPHA.set('W');
        UP_ALPHA.set('X');
        UP_ALPHA.set('Y');
        UP_ALPHA.set('Z');
    }

    /**
     * alpha = lowalpha | upalpha
     */
    public final static BitSet ALPHA = new BitSet();

    static {
        ALPHA.or(LOW_ALPHA);
        ALPHA.or(UP_ALPHA);
    }

    /**
     * digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
     */
    public final static BitSet DIGIT = new BitSet();

    static {
        DIGIT.set('0');
        DIGIT.set('1');
        DIGIT.set('2');
        DIGIT.set('3');
        DIGIT.set('4');
        DIGIT.set('5');
        DIGIT.set('6');
        DIGIT.set('7');
        DIGIT.set('8');
        DIGIT.set('9');
    }

    /**
     * alphanum = alpha | digit
     */
    public final static BitSet ALPHANUM = new BitSet();

    static {
        ALPHANUM.or(ALPHA);
        ALPHANUM.or(DIGIT);
    }

    /**
     * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
     */
    public final static BitSet UNRESERVED = new BitSet();

    static {
        UNRESERVED.or(ALPHA);
        UNRESERVED.or(DIGIT);
        UNRESERVED.set('-');
        UNRESERVED.set('.');
        UNRESERVED.set('_');
        UNRESERVED.set('~');
    }

    /**
     * pchar = unreserved | escaped | sub-delims | ":" | "@"
     * <p/>
     * Note: we don't allow escaped here since we will escape it ourselves, so we don't want to allow them in the
     * unescaped sequences
     */
    public final static BitSet PCHAR = new BitSet();

    static {
        PCHAR.or(UNRESERVED);
        PCHAR.or(SUB_DELIMS);
        PCHAR.set(':');
        PCHAR.set('@');
    }

    /**
     * path_segment = pchar <without> ";"
     */
    public final static BitSet PATH_SEGMENT = new BitSet();

    static {
        PATH_SEGMENT.or(PCHAR);
        // deviate from the RFC in order to disallow the path param separator
        PATH_SEGMENT.clear(';');
    }

    /**
     * path_param_name = pchar <without> ";" | "="
     */
    public final static BitSet PATH_PARAM_NAME = new BitSet();

    static {
        PATH_PARAM_NAME.or(PCHAR);
        // deviate from the RFC in order to disallow the path param separators
        PATH_PARAM_NAME.clear(';');
        PATH_PARAM_NAME.clear('=');
    }

    /**
     * path_param_value = pchar <without> ";"
     */
    public final static BitSet PATH_PARAM_VALUE = new BitSet();

    static {
        PATH_PARAM_VALUE.or(PCHAR);
        // deviate from the RFC in order to disallow the path param separator
        PATH_PARAM_VALUE.clear(';');
    }

    /**
     * query = pchar / "/" / "?"
     */
    public final static BitSet QUERY = new BitSet();

    static {
        QUERY.or(PCHAR);
        QUERY.set('/');
        QUERY.set('?');
        // deviate from the RFC to disallow separators such as "=", "@" and the famous "+" which is treated as a space
        // when decoding
        QUERY.clear('=');
        QUERY.clear('&');
        QUERY.clear('+');
    }

    /**
     * fragment = pchar / "/" / "?"
     */
    public final static BitSet FRAGMENT = new BitSet();

    static {
        FRAGMENT.or(PCHAR);
        FRAGMENT.set('/');
        FRAGMENT.set('?');
    }

    /**
     * Encodes a string to be a valid path parameter name, which means it can contain PCHAR* without "=" or ";". Uses
     * UTF-8.
     */
    public static String encodePathParamName(final String pathParamName) {
        try {
            return encodePart(pathParamName, "UTF-8", PATH_PARAM_NAME);
        }
        catch (final UnsupportedEncodingException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Encodes a string to be a valid path parameter value, which means it can contain PCHAR* without ";". Uses UTF-8.
     */
    public static String encodePathParamValue(final String pathParamValue) {
        try {
            return encodePart(pathParamValue, "UTF-8", PATH_PARAM_VALUE);
        }
        catch (final UnsupportedEncodingException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Encodes a string to be a valid query, which means it can contain PCHAR* | "?" | "/" without "=" | "&" | "+". Uses
     * UTF-8.
     */
    public static String encodeQueryNameOrValue(final String queryNameOrValue) {
        try {
            return encodePart(queryNameOrValue, "UTF-8", QUERY);
        }
        catch (final UnsupportedEncodingException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Encodes a string to be a valid query with no parenthesis, which means it can contain PCHAR* | "?" | "/" without
     * "=" | "&" | "+" | "(" | ")". It strips parenthesis. Uses UTF-8.
     */
    public static String encodeQueryNameOrValueNoParen(final String queryNameOrValueNoParen) {
        try {
            String query = encodePart(queryNameOrValueNoParen, "UTF-8", QUERY);
            query = query.replace("(", "");
            return query.replace(")", "");
        }
        catch (final UnsupportedEncodingException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Encodes a string to be a valid path segment, which means it can contain PCHAR* only (do not put path parameters or
     * they will be escaped. Uses UTF-8.
     */
    public static String encodePathSegment(final String pathSegment) {
        try {
            return encodePart(pathSegment, "UTF-8", PATH_SEGMENT);
        }
        catch (final UnsupportedEncodingException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Encodes a string to be a valid URI part, with the given characters allowed. The rest will be encoded.
     *
     * @throws UnsupportedEncodingException
     */
    public static String encodePart(final String part, final String charset, final BitSet allowed) throws UnsupportedEncodingException {
        if (part == null) {
            return null;
        }
        // start at *3 for the worst case when everything is %encoded on one byte
        final StringBuffer encoded = new StringBuffer(part.length() * 3);
        final char[] toEncode = part.toCharArray();
        for (final char c : toEncode) {
            if (allowed.get(c)) {
                encoded.append(c);
            } else {
                final byte[] bytes = String.valueOf(c).getBytes(charset);
                for (final byte b : bytes) {
                    // make it unsigned
                    final int u8 = b & 0xFF;
                    encoded.append(String.format("%%%1$02X", u8));
                }
            }
        }
        return encoded.toString();
	}

}
