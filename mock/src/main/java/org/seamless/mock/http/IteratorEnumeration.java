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
package org.seamless.mock.http;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Adapter to make an {@link Iterator Iterator} instance appear to be
 * an {@link Enumeration Enumeration} instance.
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 * @since Commons Collections 1.0
 */
public class IteratorEnumeration implements Enumeration {

    /**
     * The iterator being decorated.
     */
    private Iterator iterator;

    /**
     * Constructs a new <code>IteratorEnumeration</code> that will not
     * function until {@link #setIterator(Iterator) setIterator} is
     * invoked.
     */
    public IteratorEnumeration() {
        super();
    }

    /**
     * Constructs a new <code>IteratorEnumeration</code> that will use
     * the given iterator.
     *
     * @param iterator the iterator to use
     */
    public IteratorEnumeration(Iterator iterator) {
        super();
        this.iterator = iterator;
    }

    // Iterator interface
    //-------------------------------------------------------------------------

    /**
     * Returns true if the underlying iterator has more elements.
     *
     * @return true if the underlying iterator has more elements
     */
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    /**
     * Returns the next element from the underlying iterator.
     *
     * @return the next element from the underlying iterator.
     * @throws java.util.NoSuchElementException
     *          if the underlying iterator has no
     *          more elements
     */
    public Object nextElement() {
        return iterator.next();
    }

    // Properties
    //-------------------------------------------------------------------------

    /**
     * Returns the underlying iterator.
     *
     * @return the underlying iterator
     */
    public Iterator getIterator() {
        return iterator;
    }

    /**
     * Sets the underlying iterator.
     *
     * @param iterator the new underlying iterator
     */
    public void setIterator(Iterator iterator) {
        this.iterator = iterator;
    }

}
