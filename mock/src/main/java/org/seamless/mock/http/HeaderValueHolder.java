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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Internal helper class that serves as value holder for request headers.
 *
 * @author Juergen Hoeller
 * @author Rick Evans
 * @since 2.0.1
 */
class HeaderValueHolder
{

   private final List values = new LinkedList();


   public void setValue(Object value)
   {
      this.values.clear();
      this.values.add(value);
   }

   public void addValue(Object value)
   {
      this.values.add(value);
   }

   public void addValues(Collection values)
   {
      this.values.addAll(values);
   }

   public void addValueArray(Object values)
   {
      Object[] arr = toObjectArray(values);
      this.values.addAll(Arrays.asList(arr));
   }

   public List getValues()
   {
      return Collections.unmodifiableList(this.values);
   }

   public Object getValue()
   {
      return (!this.values.isEmpty() ? this.values.get(0) : null);
   }


   /**
    * Find a HeaderValueHolder by name, ignoring casing.
    *
    * @param headers the Map of header names to HeaderValueHolders
    * @param name    the name of the desired header
    * @return the corresponding HeaderValueHolder,
    *         or <code>null</code> if none found
    */
   public static HeaderValueHolder getByName(Map headers, String name)
   {
      for (Iterator it = headers.keySet().iterator(); it.hasNext();)
      {
         String headerName = (String) it.next();
         if (headerName.equalsIgnoreCase(name))
         {
            return (HeaderValueHolder) headers.get(headerName);
         }
      }
      return null;
   }

   public static Object[] toObjectArray(Object source)
   {
      if (source instanceof Object[])
      {
         return (Object[]) source;
      }
      if (source == null)
      {
         return new Object[0];
      }
      if (!source.getClass().isArray())
      {
         throw new IllegalArgumentException("Source is not an array: " + source);
      }
      int length = Array.getLength(source);
      if (length == 0)
      {
         return new Object[0];
      }
      Class wrapperType = Array.get(source, 0).getClass();
      Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
      for (int i = 0; i < length; i++)
      {
         newArray[i] = Array.get(source, i);
      }
      return newArray;
	}

}