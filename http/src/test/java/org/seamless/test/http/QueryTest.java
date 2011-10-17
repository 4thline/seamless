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

package org.seamless.test.http;

import org.seamless.http.Query;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Christian Bauer
 */
public class QueryTest {

    @Test
    public void createQuery() {
        Query query = new Query();
        assertTrue(query.isEmpty());
        assertEquals(query.getMap().size(), 0);
        assertEquals(query.getMapWithLists().size(), 0);
        assertEquals(query.get("foo"), "");
        assertEquals(query.toString(), "");

        query = new Query("foo=bar&baz=abc&baz=def");
        assertEquals(query.getMap().size(), 2);
        assertEquals(query.getMapWithLists().size(), 2);
        assertEquals(query.get("foo"), "bar");
        assertEquals(query.get("baz"), "abc");
        assertEquals(query.getValues("baz").length, 2);
        assertEquals(query.getValues("baz")[0], "abc");
        assertEquals(query.getValues("baz")[1], "def");
        assertEquals(query.getValuesAsList("baz").size(), 2);
        assertEquals(query.getValuesAsList("baz").get(0), "abc");
        assertEquals(query.getValuesAsList("baz").get(1), "def");

        assertEquals(query.toString(), "foo=bar&baz=abc&baz=def");
    }

    @Test
    public void addParameter() {
        Query query = new Query();
        query = query.cloneAndAdd("foo", "bar", "baz");
        assertEquals(query.getMap().size(), 1);
        assertEquals(query.getValues("foo").length, 2);

        query = new Query("abc=123");
        query = query.cloneAndAdd("foo", "bar", "baz");
        assertEquals(query.getMap().size(), 2);
        assertEquals(query.getValues("abc").length, 1);
        assertEquals(query.getValues("foo").length, 2);
    }
}
