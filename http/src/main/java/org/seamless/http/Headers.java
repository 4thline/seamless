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

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Unifies various HTTP header representations.
 * <p>
 * Snoracle, do you think you could add something like this to JDK 11g? Or did you kick out
 * the guy who knows how HTTP works so Larry can get his new jet filled up?
 * </p>
 * <p>
 * TODO: This is just a case-insensitive multi-valued map of strings...
 * </p>
 *
 * @author Christian Bauer
 */
public class Headers implements Map<String, List<String>> {

    final static byte CR = 13;
    final static byte LF = 10;

    final Map<String, List<String>> map = new HashMap<String, List<String>>(32);

    public Headers() {
    }

    public Headers(Map<String, List<String>> map) {
        putAll(map);
    }

    public Headers(ByteArrayInputStream inputStream) {
        Headers headers = new Headers();
        String line = readLine(inputStream);
        String lastHeader = null;
        if (line.length() != 0) {
            do {
                char firstChar = line.charAt(0);
                if (lastHeader != null && (firstChar == ' ' || firstChar == '\t')) {
                    List<String> current = headers.get(lastHeader);
                    int lastPos = current.size() - 1;
                    String newString = current.get(lastPos) + line.trim();
                    current.set(lastPos, newString);
                } else {
                    String[] header = splitHeader(line);
                    headers.add(header[0], header[1]);
                    lastHeader = header[0];
                }

                line = readLine(inputStream);
            } while (line.length() != 0);
        }
        putAll(headers);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return key != null && key instanceof String && map.containsKey(normalize((String) key));
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public List<String> get(Object key) {
        return map.get(normalize((String) key));
    }

    public List<String> put(String key, List<String> value) {
        return map.put(normalize(key), value);
    }

    public List<String> remove(Object key) {
        return map.remove(normalize((String) key));
    }

    public void putAll(Map<? extends String, ? extends List<String>> t) {
        // Enforce key normalization!
        for (Entry<? extends String, ? extends List<String>> entry : t.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        map.clear();
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Collection<List<String>> values() {
        return map.values();
    }

    public Set<Map.Entry<String, List<String>>> entrySet() {
        return map.entrySet();
    }

    public boolean equals(Object o) {
        return map.equals(o);
    }

    public int hashCode() {
        return map.hashCode();
    }

    public String getFirstHeader(String key) {
        List<String> l = map.get(normalize(key));
        return l != null && l.size() > 0 ? l.get(0) : null;
    }

    public void add(String key, String value) {
        String k = normalize(key);
        List<String> l = map.get(k);
        if (l == null) {
            l = new LinkedList<String>();
            map.put(k, l);
        }
        l.add(value);
    }

    public void set(String key, String value) {
        LinkedList<String> l = new LinkedList<String>();
        l.add(value);
        put(key, l);
    }

    private String normalize(String key) {
        if (key == null) return null;
        if (key.length() == 0) return key;
        char[] b;
        String s;
        b = key.toCharArray();
        if (b[0] >= 'a' && b[0] <= 'z') {
            b[0] = (char) (b[0] - ('a' - 'A'));
        }
        for (int i = 1; i < key.length(); i++) {
            if (b[i] >= 'A' && b[i] <= 'Z') {
                b[i] = (char) (b[i] + ('a' - 'A'));
            }
        }
        return new String(b);
    }

    public static String readLine(ByteArrayInputStream is) {
        StringBuilder sb = new StringBuilder(64);
        int nextByte;
        while((nextByte = is.read()) != -1) {
            char nextChar = (char) nextByte;
            if (nextChar == CR) {
                    nextByte = (char) is.read();
                    if (nextByte == LF) {
                        break;
                    }
            } else if (nextChar == LF) {
                break;
            }

            sb.append(nextChar);
        }
        return sb.toString();
    }

    protected String[] splitHeader(String sb) {
        int nameStart;
        int nameEnd;
        int colonEnd;
        int valueStart;
        int valueEnd;

        nameStart = findNonWhitespace(sb, 0);
        for (nameEnd = nameStart; nameEnd < sb.length(); nameEnd++) {
            char ch = sb.charAt(nameEnd);
            if (ch == ':' || Character.isWhitespace(ch)) {
                break;
            }
        }

        for (colonEnd = nameEnd; colonEnd < sb.length(); colonEnd++) {
            if (sb.charAt(colonEnd) == ':') {
                colonEnd++;
                break;
            }
        }

        valueStart = findNonWhitespace(sb, colonEnd);
        valueEnd = findEndOfString(sb);

        // This gets a bit messy because there are really HTTP headers without values (go figure...)
        return new String[]
                {
                        sb.substring(nameStart, nameEnd),
                        sb.length() >= valueStart && sb.length() >= valueEnd && valueStart < valueEnd
                                ? sb.substring(valueStart, valueEnd)
                                : null
                };
    }

    protected int findNonWhitespace(String sb, int offset) {
        int result;
        for (result = offset; result < sb.length(); result++) {
            if (!Character.isWhitespace(sb.charAt(result))) {
                break;
            }
        }
        return result;
    }

    protected int findEndOfString(String sb) {
        int result;
        for (result = sb.length(); result > 0; result--) {
            if (!Character.isWhitespace(sb.charAt(result - 1))) {
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder headerString = new StringBuilder();
        for (Entry<String, List<String>> headerEntry : entrySet()) {
            StringBuilder headerLine = new StringBuilder();

            headerLine.append(headerEntry.getKey()).append(": ");

            for (String v : headerEntry.getValue()) {
                headerLine.append(v).append(",");
            }
            headerLine.delete(headerLine.length()-1, headerLine.length());
            headerString.append(headerLine).append("\r\n");
        }
        return headerString.toString();
    }

}