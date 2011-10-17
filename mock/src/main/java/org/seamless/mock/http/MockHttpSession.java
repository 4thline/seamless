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

/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, version 2 of
 * the License.
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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author Gavin King
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 */
@SuppressWarnings("deprecation")
public class MockHttpSession implements HttpSession
{

   private Map<String, Object> attributes = new HashMap<String, Object>();
   private boolean isInvalid;
   private ServletContext servletContext;

   public MockHttpSession() {}

   public MockHttpSession(ServletContext servletContext)
   {
      this.servletContext = servletContext;
   }

   public boolean isInvalid()
   {
      return isInvalid;
   }

   public long getCreationTime()
   {
      return 0;
   }

   public String getId()
   {
      return null;
   }

   public long getLastAccessedTime()
   {
      return 0;
   }

   private int maxInactiveInterval;

   public void setMaxInactiveInterval(int max)
   {
      maxInactiveInterval = max;
   }

   public int getMaxInactiveInterval()
   {
      return maxInactiveInterval;
   }

   public HttpSessionContext getSessionContext()
   {
      throw new UnsupportedOperationException();
   }

   public Object getAttribute(String att)
   {
      return attributes.get(att);
   }

   public Object getValue(String att)
   {
      return getAttribute(att);
   }

   public Enumeration getAttributeNames()
   {
      return new IteratorEnumeration( attributes.keySet().iterator() );
   }

   public String[] getValueNames()
   {
      return attributes.keySet().toArray( new String[0] );
   }

   public void setAttribute(String att, Object value)
   {
      if (value==null)
      {
         attributes.remove(att);
      }
      else
      {
         attributes.put(att, value);
      }
   }

   public void putValue(String att, Object value)
   {
      setAttribute(att, value);
   }

   public void removeAttribute(String att)
   {
      attributes.remove(att);
   }

   public void removeValue(String att)
   {
      removeAttribute(att);
   }

   public void invalidate()
   {
      attributes.clear();
      isInvalid = true;
   }

   public boolean isNew()
   {
      return false;
   }

   public Map<String, Object> getAttributes()
   {
      return attributes;
   }

   public ServletContext getServletContext()
   {
      return servletContext;
   }

   /*public void clear() {
      attributes.clear();
      isInvalid = false;
   }*/

   
}
